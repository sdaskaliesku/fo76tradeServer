package com.manson.fo76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.manson.domain.config.ArmorConfig;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.config.XTranslatorConfig;
import com.manson.fo76.domain.dto.ItemConfig;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameConfigHolderService {

  private static final String LEG_MODS_CONFIG_FILE = "legendaryMods.config.json";
  private static final String AMMO_TYPES_CONFIG_FILE = "ammo.types.json";
  private static final String ARMOR_CONFIG_FILE = "armor.config.json";
  private static final String NAME_MODIFIERS_CONFIG_FILE = "name.modifiers.json";
  private static final String WEAPON_NAMES_CONFIG_FILE = "weapons.config.json";
  private static final String PLAN_RECIPES_CONFIG_FILE = "plans.config.json";
  private static final String ARMOR_NAMES_CONFIG_FILE = "armor.names.config.json";
  private static final TypeReference<List<LegendaryModDescriptor>> LEG_MOD_TYPE_REF = new TypeReference<List<LegendaryModDescriptor>>() {
  };
  private static final TypeReference<List<XTranslatorConfig>> X_TRANSLATOR_TYPE_REF = new TypeReference<List<XTranslatorConfig>>() {
  };
  private static final TypeReference<List<ArmorConfig>> ARMOR_CONFIG_TYPE_REF = new TypeReference<List<ArmorConfig>>() {
  };
  private static final TypeReference<List<ItemConfig>> ITEM_CONFIG_TYPE_REF = new TypeReference<List<ItemConfig>>() {
  };
  private final List<LegendaryModDescriptor> legModsConfig;
  private final List<XTranslatorConfig> ammoTypes;
  private final List<XTranslatorConfig> nameModifiers;
  private final List<ArmorConfig> armorConfigs;
  private final List<ItemConfig> weaponNames;
  private final List<ItemConfig> planNames;
  private final List<ItemConfig> armorNames;

  @Autowired
  public GameConfigHolderService(ObjectMapper objectMapper) {
    this.legModsConfig = loadConfig(objectMapper, LEG_MODS_CONFIG_FILE, LEG_MOD_TYPE_REF,
        XTranslatorConfig::getEnabled);
    this.ammoTypes = loadConfig(objectMapper, AMMO_TYPES_CONFIG_FILE, X_TRANSLATOR_TYPE_REF,
        XTranslatorConfig::getEnabled);
    this.nameModifiers = loadConfig(objectMapper, NAME_MODIFIERS_CONFIG_FILE, X_TRANSLATOR_TYPE_REF,
        XTranslatorConfig::getEnabled);
    this.armorConfigs = loadConfig(objectMapper, ARMOR_CONFIG_FILE, ARMOR_CONFIG_TYPE_REF, (x) -> true);
    this.weaponNames = loadConfig(objectMapper, WEAPON_NAMES_CONFIG_FILE, ITEM_CONFIG_TYPE_REF, ItemConfig::isEnabled);
    this.planNames = loadConfig(objectMapper, PLAN_RECIPES_CONFIG_FILE, ITEM_CONFIG_TYPE_REF, ItemConfig::isEnabled);
    this.armorNames = loadConfig(objectMapper, ARMOR_NAMES_CONFIG_FILE, ITEM_CONFIG_TYPE_REF, ItemConfig::isEnabled);
  }


  private static <T> List<T> loadConfig(ObjectMapper objectMapper, String file, TypeReference<List<T>> typeReference,
      Predicate<T> predicate) {
    try {
      @SuppressWarnings("UnstableApiUsage")
      URL resource = Resources.getResource(file);
      return objectMapper.readValue(resource, typeReference).stream().filter(predicate).collect(Collectors.toList());
    } catch (Exception e) {
      log.error("Error while loading game config", e);
    }

    return Collections.emptyList();
  }

  public List<LegendaryModDescriptor> getLegModsConfig() {
    return legModsConfig;
  }

  public List<XTranslatorConfig> getAmmoTypes() {
    return ammoTypes;
  }

  public List<XTranslatorConfig> getNameModifiers() {
    return nameModifiers;
  }

  public List<ArmorConfig> getArmorConfigs() {
    return armorConfigs;
  }

  public List<ItemConfig> getWeaponNames() {
    return weaponNames;
  }

  public List<ItemConfig> getPlanNames() {
    return planNames;
  }

  public List<ItemConfig> getArmorNames() {
    return armorNames;
  }
}
