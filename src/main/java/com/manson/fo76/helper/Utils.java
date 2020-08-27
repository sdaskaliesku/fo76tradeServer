package com.manson.fo76.helper;

import com.manson.fo76.domain.LegendaryModDescriptor;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.LegendaryMod;
import com.manson.fo76.domain.dto.StatsDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.enums.DamageType;
import com.manson.fo76.domain.items.enums.FilterFlag;
import com.manson.fo76.domain.items.enums.ItemCardText;
import com.manson.fo76.domain.items.item_card.ItemCardEntry;
import com.manson.fo76.service.JsonParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {

  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  private static final Set<ItemCardText> IGNORED_CARDS = new HashSet<>();

  static {
    IGNORED_CARDS.add(ItemCardText.LEG_MODS);
    IGNORED_CARDS.add(ItemCardText.DESC);
  }

  private Utils() {
  }

  public static List<ItemDTO> convertItems(List<ItemDescriptor> items, List<LegendaryModDescriptor> legendaryMods, User user) {
    return items.stream().map(item -> convertItem(item, legendaryMods, user))
        .collect(Collectors.toList());
  }

  private static Number silentParse(String value) {
    try {
      return Double.valueOf(value);
    } catch (Exception ignored) {

    }
    return -1;
  }

  private static FilterFlag findFilterFlag(ItemDescriptor item) {
    FilterFlag filterFlag = item.getFilterFlagEnum();
    try {
      if (filterFlag == FilterFlag.WEAPON) {
        for (ItemCardEntry itemCardEntry : item.getItemCardEntries()) {
          if (itemCardEntry.getDamageTypeEnum() == DamageType.AMMO) {
            filterFlag = FilterFlag.WEAPON_RANGED;
            break;
          }
          ItemCardText cardText = itemCardEntry.getItemCardText();
          if (cardText == ItemCardText.ROF && silentParse(itemCardEntry.getValue()).doubleValue() > 0) {
            filterFlag = FilterFlag.WEAPON_RANGED;
            break;
          }
          if (cardText == ItemCardText.MELEE_SPEED) {
            filterFlag = FilterFlag.WEAPON_MELEE;
            break;
          }
          if (cardText == ItemCardText.RNG && silentParse(itemCardEntry.getValue()).doubleValue() > 0) {
            filterFlag = FilterFlag.WEAPON_THROWN;
            break;
          }
        }
      }
      if (filterFlag == FilterFlag.ARMOR && item.getCurrentHealth() == -1) {
        filterFlag = FilterFlag.ARMOR_OUTFIT;
      }
    } catch (Exception e) {
      LOGGER.error("Error while looking for specific filter flag {}", item, e);
    }
    return filterFlag;
  }

  public static ItemDTO convertItem(ItemDescriptor item, List<LegendaryModDescriptor> legendaryMods, User user) {
    Map<String, Object> objectMap = JsonParser.objectToMap(item);
    objectMap.put("filterFlag", findFilterFlag(item));
    ItemDTO itemDTO = JsonParser.mapToItemDTO(objectMap);
    if (Objects.isNull(itemDTO)) {
      return null;
    }
    itemDTO.setOwnerId(user.getId());
    itemDTO.setOwnerName(user.getName());
    itemDTO.setStats(processItemCardEntries(item, itemDTO, legendaryMods));
    return itemDTO;
  }

  private static List<LegendaryMod> processLegendaryMods(ItemCardEntry itemCardEntry, List<LegendaryModDescriptor> legModsDescr) {
    List<LegendaryMod> mods = new ArrayList<>();
    if (itemCardEntry.getItemCardText() == ItemCardText.DESC) {
      String[] strings = itemCardEntry.getValue().split("\n");
      if (ArrayUtils.isNotEmpty(strings)) {
        for (String mod: strings) {
          mod = mod.trim();
          LegendaryMod legendaryMod = new LegendaryMod(mod);
          for (LegendaryModDescriptor descriptor : legModsDescr) {
            if (descriptor.isTheSameMod(mod)) {
              legendaryMod.setAbbreviation(descriptor.getAbbreviation());
              legendaryMod.setStar(descriptor.getStar());
              break;
            }
          }
          mods.add(legendaryMod);
        }
      }
    }
    if (CollectionUtils.isNotEmpty(mods)) {
      mods.sort(Comparator.comparingInt(LegendaryMod::getStar));
    }
    return mods;
  }

  private static List<StatsDTO> processItemCardEntries(ItemDescriptor item, ItemDTO itemDTO, List<LegendaryModDescriptor> legModsDescr) {
    List<StatsDTO> stats = new ArrayList<>();
    if (CollectionUtils.isEmpty(item.getItemCardEntries())) {
      return stats;
    }
    for (ItemCardEntry itemCardEntry : item.getItemCardEntries()) {
      StatsDTO statsDTO = convertItemStats(itemCardEntry);
      if (statsDTO != null) {
        stats.add(statsDTO);
      } else if (itemCardEntry.getItemCardText() == ItemCardText.DESC) {
        if (itemDTO.getFilterFlag() != null && itemDTO.getFilterFlag().isHasStarMods()) {
          List<LegendaryMod> legendaryMods = processLegendaryMods(itemCardEntry, legModsDescr);
          if (CollectionUtils.isEmpty(legendaryMods)) {
            continue;
          }
          itemDTO.setLegendaryMods(legendaryMods);
          String abbreviation = legendaryMods.stream().map(LegendaryMod::getAbbreviation).collect(Collectors.joining("/"));
          itemDTO.setAbbreviation(abbreviation);
          // TODO: temp solution for temp page, needs to be removed
          itemDTO.setLegendaryModsTemp(
              legendaryMods.stream().filter(Objects::nonNull).map(LegendaryMod::getValue).collect(Collectors.toList()));
        } else {
          itemDTO.setDescription(itemCardEntry.getValue());
        }
      }
    }
    return stats;
  }

  private static StatsDTO convertItemStats(ItemCardEntry itemCardEntry) {
    if (IGNORED_CARDS.contains(itemCardEntry.getItemCardText())) {
      return null;
    }
    Map<String, Object> objectMap = JsonParser.objectToMap(itemCardEntry);
    objectMap.put("text", itemCardEntry.getItemCardText());
    objectMap.put("damageType", itemCardEntry.getDamageTypeEnum());
    return JsonParser.mapToStatsDTO(objectMap);
  }

  public static boolean validatePassword(User user, User userInDb) {
    return StringUtils.equals(user.getPassword(), userInDb.getPassword());
  }

}
