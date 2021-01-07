package com.manson.fo76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.config.AppConfig;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonParser {

  private static final TypeReference<Map<String, Object>> TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {
  };
  private static final ObjectMapper OM = AppConfig.Companion.getObjectMapper();

  public static Map<String, Object> objectToMap(Object o) {
    try {
      return OM.convertValue(o, TYPE_REFERENCE);
    } catch (Exception e) {
      log.error("Error converting object to map: {}", o, e);
    }
    return null;
  }

  public static <T> T mapToClass(Map<String, Object> map, Class<T> clazz) {
    try {
      return OM.convertValue(map, clazz);
    } catch (Exception e) {
      log.error("Converting map to class: {}", map, e);
    }
    return null;
  }

}
