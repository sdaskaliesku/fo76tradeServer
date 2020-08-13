package com.manson.fo76.helper;

import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.LegendaryMod;
import com.manson.fo76.domain.dto.StatsDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.enums.ItemCardText;
import com.manson.fo76.domain.items.item_card.ItemCardEntry;
import com.manson.fo76.service.JsonParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class Utils {

  private static final Set<ItemCardText> IGNORED_CARDS = new HashSet<>();

  static {
    IGNORED_CARDS.add(ItemCardText.LEG_MODS);
    IGNORED_CARDS.add(ItemCardText.DESC);
  }

  private Utils() {
  }

  public static List<ItemDTO> convertItems(List<ItemDescriptor> items, User user) {
    return items.stream().map(item -> convertItem(item, user))
        .collect(Collectors.toList());
  }

  public static ItemDTO convertItem(ItemDescriptor item, User user) {
    Map<String, Object> objectMap = JsonParser.objectToMap(item);
    objectMap.put("filterFlag", item.getFilterFlagEnum());
    ItemDTO itemDTO = JsonParser.mapToItemDTO(objectMap);
    itemDTO.setOwnerId(user.getId());
    itemDTO.setOwnerName(user.getName());
    itemDTO.setStats(processItemCardEntries(item, itemDTO));
    return itemDTO;
  }

  private static List<LegendaryMod> processLegendaryMods(ItemCardEntry itemCardEntry) {
    List<LegendaryMod> mods = null;
    if (itemCardEntry.getItemCardText() == ItemCardText.DESC) {
      String[] strings = itemCardEntry.getValue().split("\n");
      if (ArrayUtils.isNotEmpty(strings)) {
        mods = Arrays.stream(strings).map(String::trim).map(LegendaryMod::new).collect(Collectors.toList());
      }
    }
    return mods;
  }

  private static List<StatsDTO> processItemCardEntries(ItemDescriptor item, ItemDTO itemDTO) {
    List<StatsDTO> stats = new ArrayList<>();
    if (CollectionUtils.isEmpty(item.getItemCardEntries())) {
      return stats;
    }
    for (ItemCardEntry itemCardEntry : item.getItemCardEntries()) {
      StatsDTO statsDTO = convertItemStats(itemCardEntry);
      if (statsDTO != null) {
        stats.add(statsDTO);
      } else if (itemCardEntry.getItemCardText() == ItemCardText.DESC) {
        itemDTO.setLegendaryMods(processLegendaryMods(itemCardEntry));
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
