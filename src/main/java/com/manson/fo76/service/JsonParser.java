package com.manson.fo76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.AppConfig;
import com.manson.fo76.domain.ModData;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.StatsDTO;
import java.io.File;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JsonParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonParser.class);

  private static final TypeReference<Map<String, Object>> TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {
  };

  private static final ObjectMapper OM = AppConfig.getObjectMapper();

  private JsonParser() {
  }

  public static Map<String, Object> objectToMap(Object o) {
    try {
      return OM.convertValue(o, TYPE_REFERENCE);
    } catch (Exception e) {
      LOGGER.error("Error converting object to map: {}", o, e);
    }
    return null;
  }

  public static ItemDTO mapToItemDTO(Map<String, Object> map) {
    try {
      return OM.convertValue(map, ItemDTO.class);
    } catch (Exception e) {
      LOGGER.error("Error converting map to ItemDTO: {}", map, e);
    }
    return null;
  }

  public static StatsDTO mapToStatsDTO(Map<String, Object> map) {
    try {
      return OM.convertValue(map, StatsDTO.class);
    } catch (Exception e) {
      LOGGER.error("Error parsing file", e);
    }
    return null;
  }

  public static ModData parse(File file) {
    try {
      return OM.readValue(file, ModData.class);
    } catch (Exception e) {
      LOGGER.error("Error parsing file", e);
    }
    return null;
  }
}
