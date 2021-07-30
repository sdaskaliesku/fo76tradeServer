package com.manson.fo76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HudEditorConfigService extends BaseRestClient {

  private static final TypeReference<Map<String, Object>> HUD_EDITOR_CONFIG = new TypeReference<Map<String, Object>>() {
  };

  @Value("${app.config.hud.editor}")
  private String url;

  private Map<String, Object> hudEditorConfig;

  @Value("${config.hud.editor.schema}")
  private String hudEditorConfigFile;

  public HudEditorConfigService(ObjectMapper objectMapper) {
    super(objectMapper);
  }

  @PostConstruct
  public void init() {
    this.hudEditorConfig = loadConfig(objectMapper, hudEditorConfigFile, HUD_EDITOR_CONFIG);
  }

  private Map<String, Object> getConfigFromGH() {
    try {
      String response = this.client.target(url).request(MediaType.APPLICATION_JSON).get(String.class);
      return objectMapper.readValue(response, HUD_EDITOR_CONFIG);
    } catch (Exception e) {
      log.error("Error requesting config", e);
    }
    return null;
  }


  public Map<String, Object> getConfig() {
    Map<String, Object> config = getConfigFromGH();
    if (MapUtils.isEmpty(config)) {
      return hudEditorConfig;
    }
    return config;
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

  private static URL getResource(String file) {
    try {
      //noinspection UnstableApiUsage
      return Resources.getResource(file);
    } catch (Exception e) {
      log.error("Error while loading game config", e);
    }
    return null;
  }
}
