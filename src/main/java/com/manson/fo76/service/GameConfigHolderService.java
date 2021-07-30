package com.manson.fo76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.manson.domain.config.ArmorConfig;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.config.XTranslatorConfig;
import com.manson.domain.itemextractor.ItemConfig;
import com.manson.fo76.helper.Utils;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Data
public class GameConfigHolderService {

  private static final TypeReference<List<LegendaryModDescriptor>> LEG_MOD_TYPE_REF = new TypeReference<List<LegendaryModDescriptor>>() {
  };
  private static final TypeReference<List<XTranslatorConfig>> X_TRANSLATOR_TYPE_REF = new TypeReference<List<XTranslatorConfig>>() {
  };
  private static final TypeReference<List<ArmorConfig>> ARMOR_CONFIG_TYPE_REF = new TypeReference<List<ArmorConfig>>() {
  };
  private static final TypeReference<List<ItemConfig>> ITEM_CONFIG_TYPE_REF = new TypeReference<List<ItemConfig>>() {
  };
  private static final TypeReference<Map<String, String>> SPECIAL_CASES_CONFIG_REF = new TypeReference<Map<String, String>>() {
  };
  @Value("${game.config.leg.mods.file}")
  private String legModsConfigFile;

  @Value("${game.config.ammo.file}")
  private String ammoTypesConfigFile;

  @Value("${game.config.armor.file}")
  private String armorConfigFile;

  @Value("${game.config.weapon.file}")
  private String weaponNamesConfigFile;

  @Value("${game.config.plan.file}")
  private String planRecipesConfigFile;

  @Value("${game.config.armor.name.file}")
  private String armorNamesConfigFile;

  @Value("${game.config.weapon.special.file}")
  private String specialCasesConfigFile;

  @Autowired
  private ObjectMapper objectMapper;

  private List<LegendaryModDescriptor> legModsConfig;
  private List<XTranslatorConfig> ammoTypes;
  private List<ArmorConfig> armorConfigs;
  private List<ItemConfig> weaponNames;
  private List<ItemConfig> planNames;
  private List<ItemConfig> armorNames;
  private Map<String, String> specialCasesConfig;

  @PostConstruct
  public void init() {
    this.legModsConfig = loadConfig(objectMapper, legModsConfigFile, LEG_MOD_TYPE_REF,
        XTranslatorConfig::isEnabled);
    this.ammoTypes = loadConfig(objectMapper, ammoTypesConfigFile, X_TRANSLATOR_TYPE_REF,
        XTranslatorConfig::isEnabled);
    this.armorConfigs = loadConfig(objectMapper, armorConfigFile, ARMOR_CONFIG_TYPE_REF, (x) -> true);
    this.weaponNames = loadConfig(objectMapper, weaponNamesConfigFile, ITEM_CONFIG_TYPE_REF, ItemConfig::isEnabled);
    this.planNames = loadConfig(objectMapper, planRecipesConfigFile, ITEM_CONFIG_TYPE_REF, ItemConfig::isEnabled);
    this.armorNames = loadConfig(objectMapper, armorNamesConfigFile, ITEM_CONFIG_TYPE_REF, ItemConfig::isEnabled);
    this.specialCasesConfig = loadConfig(objectMapper, specialCasesConfigFile, SPECIAL_CASES_CONFIG_REF);
    sortByName(this.weaponNames);
    sortByName(this.planNames);
    sortByName(this.armorNames);
  }

  private static void sortByName(List<ItemConfig> itemConfigs) {
    itemConfigs.sort(
        (o1, o2) -> Utils.getDefaultText(o2.getTexts()).length() - Utils.getDefaultText(o1.getTexts()).length());
  }

  private static URL getResource(String file) {
    try {
      //noinspection UnstableApiUsage
      return Resources.getResource(file);
    } catch (Exception e) {
      log.error("Error while loading game config", e);
    }
    return null;
  }

  private static <K, V> Map<K, V> loadConfig(ObjectMapper objectMapper, String file,
      TypeReference<Map<K, V>> typeReference) {
    try {
      URL resource = getResource(file);
      return objectMapper.readValue(resource, typeReference);
    } catch (Exception e) {
      log.error("Error while loading game config", e);
    }
    return Collections.emptyMap();
  }


  private static <T> List<T> loadConfig(ObjectMapper objectMapper, String file, TypeReference<List<T>> typeReference,
      Predicate<T> predicate) {
    try {
      URL resource = getResource(file);
      return objectMapper.readValue(resource, typeReference).stream().filter(predicate).collect(Collectors.toList());
    } catch (Exception e) {
      log.error("Error while loading game config", e);
    }

    return Collections.emptyList();
  }

}
