package com.fo76;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.itemextractor.ItemConfig;
import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.LegendaryMod;
import com.manson.fo76.config.AppConfig;
import com.manson.fo76.domain.ReportedItem;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.service.Fed76Service;
import com.manson.fo76.service.GameConfigHolderService;
import com.manson.fo76.service.GameConfigService;
import com.manson.fo76.service.ItemConverterService;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

public class ReportedItemsTests {

  private static final ObjectMapper OM = AppConfig.getObjectMapper();
  private static final TypeReference<List<ReportedItem>> REPORTED_ITEMS_REF = new TypeReference<List<ReportedItem>>() {
  };

  private static final GameConfigHolderService gameConfigHolderService = new GameConfigHolderService(OM);
  private static final GameConfigService gameConfigService = new GameConfigService(gameConfigHolderService);
  private static final Fed76Service fed76Service = new Fed76Service(OM);
  private static final ItemConverterService itemConverterService = new ItemConverterService(gameConfigService, fed76Service);

  private static final List<ReportedItem> listReportedItems() throws Exception {
    File file = new File("src/test/resources/reported.items.json");
    return OM.readValue(file, REPORTED_ITEMS_REF);
  }


  @Test
  void testReportedItems() throws Exception {
    List<ReportedItem> reportedItems = listReportedItems();
    for (ReportedItem reportedItem: reportedItems) {
      ItemResponse item = reportedItem.getItem();
      if (item.getItemDetails() == null) {
        continue;
      }
      List<LegendaryMod> legendaryMods = item.getItemDetails().getLegendaryMods();
      if (CollectionUtils.isEmpty(legendaryMods)) {
        continue;
      }
      List<String> modTexts = legendaryMods.stream().map(LegendaryMod::getValue).filter(Objects::nonNull).collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(modTexts)) {
        System.out.println(modTexts);
        List<LegendaryMod> mods = itemConverterService.getLegendaryMods(modTexts, item.getFilterFlag());
        System.out.println(mods);
      }
    }
  }

  @Test
  void testItemNames() throws Exception {
    List<ReportedItem> reportedItems = listReportedItems();
    for (ReportedItem reportedItem: reportedItems) {
      ItemResponse item = reportedItem.getItem();
      String text = item.getText();
      FilterFlag filterFlag = item.getFilterFlag();
      ItemConfig itemConfig = itemConverterService.findItemConfig(text, filterFlag);
      if (Objects.isNull(itemConfig)) {
//        System.out.println(text + "\t is null");
        continue;
      }
      System.out.println(text + "\t" + Utils.getDefaultText(itemConfig.getTexts()));
    }
  }
}
