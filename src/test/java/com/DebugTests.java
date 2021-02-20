package com;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.fed76.mapping.Fed76ApiMappingEntry;
import com.manson.domain.fed76.mapping.MappingResponse;
import com.manson.fo76.config.AppConfig;
import com.manson.fo76.service.Fed76Service;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

@org.junit.jupiter.api.Disabled
public class DebugTests {

  private static final TypeReference<List<LegendaryModDescriptor>> LEG_MOD_TYPE_REF = new TypeReference<List<LegendaryModDescriptor>>() {
  };

  @Test
  void updateLegModsConfigWithAbbreviations() throws Exception {
    ObjectMapper om = AppConfig.getObjectMapper();
    File file = new File("src/main/resources/legendaryMods.config.json");
    Fed76Service fed76Service = new Fed76Service(om);
    Map<String, Fed76ApiMappingEntry> mapping = fed76Service.getMapping().getEffects().getById();
    List<LegendaryModDescriptor> descriptors = om.readValue(file, LEG_MOD_TYPE_REF);
    for (LegendaryModDescriptor descriptor : descriptors) {
      List<String> keys = Arrays.asList(descriptor.getGameId(), descriptor.getGameId().toLowerCase(), descriptor.getGameId().toUpperCase());
      for (String key: keys) {
        if (mapping.containsKey(key)) {
          List<String> abbreviations = mapping.get(key).getQueries();
          String abbreviation = abbreviations.get(0);
          descriptor.setAbbreviation(abbreviation);
          descriptor.setAdditionalAbbreviations(abbreviations);
          break;
        }
      }
    }
    om.writeValue(file, descriptors);
  }

  @Test
  void testLegModsConfig() throws Exception {
    ObjectMapper om = AppConfig.getObjectMapper();
    File file = new File("src/main/resources/legendaryMods.config.json");
//    Fed76Service fed76Service = new Fed76Service(om, null);
//    Map<String, Fed76ApiMappingEntry> mapping = fed76Service.getMapping().getEffects().getById();
    List<LegendaryModDescriptor> descriptors = om.readValue(file, LEG_MOD_TYPE_REF);
    for (LegendaryModDescriptor descriptor : descriptors) {
      String itemType = descriptor.getItemType().name().replace("_", "").replace("RANGED", "").replace("MELEE", "");
      if (!StringUtils.containsIgnoreCase(descriptor.getId(), itemType)) {
        System.out.println(descriptor.getGameId());
      }
    }
//    om.writeValue(file, descriptors);
  }
}
