package com.fo76.jdump;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.fed76.mapping.Fed76ApiMappingEntry;
import com.manson.domain.fed76.mapping.MappingResponse;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.fo76.config.AppConfig;
import com.manson.fo76.service.Fed76Service;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class ConfigUpdaterTest {

  private static final String NULL = "null";
  private static final String EMPTY_OBJECT = "{}";

  private static final ObjectMapper OM = AppConfig.getObjectMapper();
  private static final TypeReference<Map<String, String>> STRING_MAP_REF = new TypeReference<Map<String, String>>() {
  };
  private static final TypeReference<List<LegendaryModDescriptor>> LEG_MOD_REF = new TypeReference<List<LegendaryModDescriptor>>() {
  };
  private static SessionFactory sessionFactory;
  private static Fed76Service fed76Service;

  @BeforeAll
  public static void beforeAll() {
//    OM.setSerializationInclusion(Include.ALWAYS);
    // disable logging
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    loggerContext.stop();
    Configuration configuration = new Configuration();
    configuration.configure();
    sessionFactory = configuration.buildSessionFactory();
    fed76Service = new Fed76Service(OM);
  }

  private static List<SqlLiteEntity> getData(String query) {
    Session session = null;
    Transaction tx = null;
    try {
      session = sessionFactory.openSession();
      tx = session.beginTransaction();
      //noinspection unchecked
      return session.createNativeQuery(query).addEntity(SqlLiteEntity.class).list();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (session != null) {
        session.flush();
      }
      if (tx != null) {
        tx.commit();
      }
    }
    return new ArrayList<>();
  }

  private static boolean isEmpty(String input) {
    boolean isBlank = StringUtils.isBlank(input);
    boolean isNull = StringUtils.equalsIgnoreCase(input, NULL);
    boolean isEmptyObject = StringUtils.equalsIgnoreCase(input, EMPTY_OBJECT);
    return isBlank || isNull || isEmptyObject;
  }

  private static Integer parseStarMod(String input) {
    try {
      String num = input.replaceAll("[^0-9]", "");
      if (StringUtils.isBlank(num) || StringUtils.length(num) > 1) {
        return 999;
      }
      return Integer.parseInt(num);
    } catch (Exception e) {
      System.out.println("Error parsing star from " + input);
    }
    return null;
  }

  @Test
  void updateLegModsConfig() throws Exception {
    List<SqlLiteEntity> data = getData(Queries.LEG_MODS);
    MappingResponse mapping = fed76Service.getMapping();
    List<LegendaryModDescriptor> descriptors = new ArrayList<>();
    for (SqlLiteEntity entity : data) {
      if (isEmpty(entity.getName()) || isEmpty(entity.getName()) || isEmpty(entity.getDesc()) || isEmpty(
          entity.getEditorid()) || isEmpty(entity.getFormid())) {
        continue;
      }

      String editorId = entity.getEditorid();
      LegendaryModDescriptor modDescriptor = new LegendaryModDescriptor();
      modDescriptor.setId(editorId);
      modDescriptor.setGameId(entity.getFormid());
      modDescriptor.setTexts(OM.readValue(entity.getName(), STRING_MAP_REF));
      modDescriptor.setTranslations(OM.readValue(entity.getDesc(), STRING_MAP_REF));
      Integer star = parseStarMod(editorId);
      if (star == null) {
        continue;
      }
      modDescriptor.setStar(star);
      if (StringUtils.containsIgnoreCase(editorId, "weapon")) {
        modDescriptor.setItemType(FilterFlag.WEAPON);
      } else if (StringUtils.containsIgnoreCase(editorId, "armor")) {
        modDescriptor.setItemType(FilterFlag.ARMOR);
      } else {
        System.out.println("Unknown item type: " + editorId);
        continue;
      }
      List<String> abbreviations = getAbbreviations(mapping, entity.getFormid());
      boolean hasAbbreviations = CollectionUtils.isNotEmpty(abbreviations);
      modDescriptor.setEnabled(hasAbbreviations);
      if (hasAbbreviations) {
        modDescriptor.setAdditionalAbbreviations(abbreviations);
        modDescriptor.setAbbreviation(abbreviations.get(0));
      }
      if (hasAbbreviations && (star > 3)) {
        System.out.println("Need for manual check: " + editorId);
      }

      descriptors.add(modDescriptor);
    }
    descriptors.sort(Comparator.comparingInt(LegendaryModDescriptor::getStar));
    OM.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/legendaryMods.config1.json"), descriptors);
  }

  private Fed76ApiMappingEntry getFedEntry(Map<String, Fed76ApiMappingEntry> map, String key) {
    List<String> keys = Arrays.asList(key, key.toLowerCase(), key.toUpperCase());
    return keys.stream().filter(map::containsKey).findFirst().map(map::get).orElse(null);
  }

  private List<String> getAbbreviations(MappingResponse response, String formId) {
    Map<String, Fed76ApiMappingEntry> byId = response.getEffects().getById();
    Fed76ApiMappingEntry fed76ApiMappingEntry = getFedEntry(byId, formId);
    if (fed76ApiMappingEntry != null) {
      return fed76ApiMappingEntry.getQueries();
    }
    return null;
  }
}
