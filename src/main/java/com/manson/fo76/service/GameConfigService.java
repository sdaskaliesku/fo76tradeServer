package com.manson.fo76.service;

import com.google.common.collect.Sets;
import com.manson.domain.config.ArmorConfig;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.fo76.ItemCardEntry;
import com.manson.domain.fo76.ItemDescriptor;
import com.manson.domain.fo76.items.enums.DamageType;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.fo76.items.enums.ItemCardText;
import com.manson.domain.itemextractor.ItemConfig;
import com.manson.domain.itemextractor.Stats;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameConfigService {

  public static final boolean POPULATE_CONFIG_FOR_EVERYTHING = true;

  private static final String DOT = ".";
  private static final Set<FilterFlag> SUPPORTED_TYPES_ARMOR = Sets
      .newHashSet(FilterFlag.ARMOR, FilterFlag.APPAREL, FilterFlag.POWER_ARMOR);
  private final GameConfigHolderService config;

  @Autowired
  public GameConfigService(GameConfigHolderService config) {
    this.config = config;
  }

  private static boolean isSameFilterFlag(FilterFlag first, FilterFlag second) {
    boolean same = first == second;
    boolean subtype = first.getSubtypes().contains(second) || second.getSubtypes().contains(first);
    return same || subtype;
  }

  private static String replace(String input) {
    if (StringUtils.isBlank(input)) {
      return input;
    }
    return input.replace(DOT, StringUtils.EMPTY).trim();
  }

  public final ItemCardText findItemCardText(ItemCardEntry cardEntry) {
    return this.findItemCardText(cardEntry.getText());
  }

  public final ItemCardText findItemCardText(String input) {
    if (StringUtils.isBlank(input)) {
      return ItemCardText.UNKNOWN;
    }
    for (ItemCardText itemCardText : ItemCardText.values()) {
      for (String value : itemCardText.getValues()) {
        if (StringUtils.equalsIgnoreCase(value, input)) {
          return itemCardText;
        }
      }
    }
    if (config.getAmmoTypes().stream().map(ammoType -> ammoType.getTexts().values()).flatMap(Collection::stream)
        .anyMatch(value -> StringUtils.equalsIgnoreCase(value, input))) {
      return ItemCardText.AMMO;
    }

    return ItemCardText.UNKNOWN;
  }

  public final LegendaryModDescriptor findLegendaryModDescriptor(String input, FilterFlag filterFlag) {
    if (StringUtils.isBlank(input)) {
      return null;
    }
    return config.getLegModsConfig()
        .stream()
        .filter(x -> x.isEnabled() && isSameFilterFlag(x.getItemType(), filterFlag))
        .filter(x -> x.isTheSameMod(input, filterFlag))
        .findFirst()
        .orElse(null);
  }

  public ItemConfig findArmorConfig(String name, FilterFlag filterFlag) {
    if (!SUPPORTED_TYPES_ARMOR.contains(filterFlag)) {
      return null;
    }
    String itemName = replace(name);
    for (ItemConfig config : config.getArmorNames()) {
      for (String text : config.getTexts().values()) {
        String cleanItemText = replace(text);
        if (StringUtils.containsIgnoreCase(itemName, cleanItemText)) {
          return config;
        }
      }
    }
    return null;
  }

  public ItemConfig findArmorConfig(ItemDescriptor item, FilterFlag filterFlag) {
    if (!POPULATE_CONFIG_FOR_EVERYTHING) {
      if (!item.isLegendary() || !item.isTradable()) {
        return null;
      }
    }
    return findArmorConfig(item.getText(), filterFlag);
  }

  public ItemConfig findPlanConfig(String itemName, FilterFlag filterFlag) {
    if (filterFlag != FilterFlag.NOTES) {
      return null;
    }
    for (ItemConfig config : config.getPlanNames()) {
      for (String text : config.getTexts().values()) {
        if (StringUtils.equalsIgnoreCase(itemName, text)) {
          return config;
        }
      }
    }
    return null;
  }

  public ItemConfig findPlanConfig(ItemDescriptor item, FilterFlag filterFlag) {
    return findPlanConfig(item.getText(), filterFlag);
  }

  public ItemConfig findWeaponConfig(String itemText, FilterFlag filterFlag) {
    String itemName = replace(itemText);
    List<ItemConfig> itemConfigs = config.getWeaponNames().stream().filter(x -> isSameFilterFlag(x.getType(), filterFlag))
        .collect(Collectors.toList());
    for (ItemConfig config : itemConfigs) {
      for (String text : config.getTexts().values()) {
        String cleanItemText = replace(text);
        if (StringUtils.containsIgnoreCase(itemName, cleanItemText)) {
          return config;
        }
      }
    }
    // TODO: add fuzzy search for not-found items?
    return null;
  }

  public ItemConfig findWeaponConfig(ItemDescriptor item, FilterFlag filterFlag) {
    if (!POPULATE_CONFIG_FOR_EVERYTHING) {
      if (!item.isLegendary() || !item.isTradable()) {
        return null;
      }
    }
    return findWeaponConfig(item.getText(), filterFlag);
  }

  private int findDamageTypeValue(List<Stats> stats, DamageType dmgType) {
    for (Stats stat : stats) {
      if (stat.getDamageType() == dmgType) {
        if (StringUtils.isNotBlank(stat.getValue())) {
          return Integer.parseInt(stat.getValue());
        }
      }
    }
    return 0;
  }

  public ArmorConfig findArmorType(int dr, int rr, int er) {
    for (ArmorConfig config : config.getArmorConfigs()) {
      if (config.getDr() == dr && config.getEr() == er && config.getRr() == rr) {
        return config;
      }
    }
    return null;
  }

  public ArmorConfig findArmorType(List<Stats> stats, String abbreviation) {
    int dr = findDamageTypeValue(stats, DamageType.BALLISTIC);
    int er = findDamageTypeValue(stats, DamageType.ENERGY);
    int rr = findDamageTypeValue(stats, DamageType.RADIATION);
    if (StringUtils.containsIgnoreCase(abbreviation, "25R")) {
      rr -= 25;
    }

    return this.findArmorType(dr, rr, er);
  }

}
