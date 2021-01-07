package com.manson.fo76.service;

import com.google.common.collect.Sets;
import com.manson.domain.config.ArmorConfig;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.config.XTranslatorConfig;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import com.manson.domain.fo76.items.enums.DamageType;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.fo76.items.enums.ItemCardText;
import com.manson.domain.fo76.items.item_card.ItemCardEntry;
import com.manson.fo76.domain.dto.ItemConfig;
import com.manson.fo76.domain.dto.ItemDescriptor;
import com.manson.fo76.domain.dto.Stats;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameConfigService {

  private static final Set<String> SPECIAL_CHARACTERS = Sets.newHashSet("¢", "$ZEUSGLYPH");
  private static final String DOT = ".";
  private static final Set<FilterFlag> SUPPORTED_TYPES_ARMOR = Sets
      .newHashSet(FilterFlag.ARMOR, FilterFlag.ARMOR_OUTFIT, FilterFlag.POWER_ARMOR);
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

  public static String cleanItemName(String name) {
    String input = name;
    for (String character : SPECIAL_CHARACTERS) {
      input = replace(input, character);
    }
    return input;
  }

  private static String replace(String input, String replacement) {
    if (StringUtils.isBlank(input)) {
      return input;
    }
    return input.replace(replacement, StringUtils.EMPTY).trim();
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
        .filter(x -> x.getEnabled() && isSameFilterFlag(x.getItemType(), filterFlag))
        .filter(x -> x.isTheSameMod(input, filterFlag))
        .findFirst()
        .orElse(null);
  }

  public ItemConfig findArmorConfig(ItemDescriptor item) {
    if (!SUPPORTED_TYPES_ARMOR.contains(item.getFilterFlagEnum()) || !item.isLegendary() || !item.isTradable()) {
      return null;
    }
    String itemName = replace(item.getText(), DOT);
    for (ItemConfig config : config.getArmorNames()) {
      for (String text : config.getTexts().values()) {
        String fedItemText = replace(text, DOT);
        if (StringUtils.containsIgnoreCase(itemName, fedItemText)) {
          return config;
        }
      }
    }
    return null;
  }

  public ItemConfig findPlanConfig(ItemDescriptor item) {
    if (item.getFilterFlagEnum() != FilterFlag.NOTES) {
      return null;
    }
    String itemName = replace(item.getText(), DOT);
    for (ItemConfig config : config.getPlanNames()) {
      for (String text : config.getTexts().values()) {
        String fedItemText = replace(text, DOT);
        if (StringUtils.containsIgnoreCase(itemName, fedItemText)) {
          return config;
        }
      }
    }
    return null;
  }

  public ItemConfig findWeaponConfig(ItemDescriptor item) {
    if (!item.isLegendary() || !item.isTradable()) {
      return null;
    }
    String itemName = replace(item.getText(), DOT);
    for (ItemConfig config : config.getWeaponNames()) {
      if (isSameFilterFlag(config.getType(), item.getFilterFlagEnum())) {
        for (String text : config.getTexts().values()) {
          String fedItemText = replace(text, DOT);
          if (StringUtils.containsIgnoreCase(itemName, fedItemText)) {
            return config;
          }
        }
      }
    }
    return null;
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

  public ArmorGrade findArmorType(int dr, int rr, int er) {
    for (ArmorConfig config : config.getArmorConfigs()) {
      if (config.getDr() == dr && config.getEr() == er && config.getRr() == rr) {
        for (ArmorGrade armorGrade : ArmorGrade.values()) {
          if (StringUtils.containsIgnoreCase(config.getArmorGrade().getValue(), armorGrade.getValue())) {
            return armorGrade;
          }
        }
      }
    }
    return ArmorGrade.Unknown;
  }

  public ArmorGrade findArmorType(List<Stats> stats, String abbreviation) {
    int dr = findDamageTypeValue(stats, DamageType.BALLISTIC);
    int er = findDamageTypeValue(stats, DamageType.ENERGY);
    int rr = findDamageTypeValue(stats, DamageType.RADIATION);
    if (StringUtils.containsIgnoreCase(abbreviation, "25R")) {
      rr -= 25;
    }

    return this.findArmorType(dr, rr, er);
  }

  public String getPossibleItemName(String name, boolean isArmor) {
    String newName = cleanItemName(name);

    for (XTranslatorConfig modifier : config.getNameModifiers()) {
      for (String text : modifier.getTexts().values()) {
        newName = replace(newName, text);
      }
    }
    if (isArmor) {
      for (ArmorGrade armorGrade : ArmorGrade.values()) {
        newName = replace(newName, armorGrade.getValue());
      }
    }
    if (StringUtils.isBlank(newName)) {
      return StringUtils.EMPTY;
    }
    return newName.trim();
  }

}
