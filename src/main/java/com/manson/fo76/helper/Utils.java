package com.manson.fo76.helper;

import com.manson.domain.itemextractor.ItemResponse;
import com.manson.fo76.config.AppConfig;
import java.io.StringWriter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

@Slf4j
public class Utils {

  public static boolean areSameItems(ItemResponse first, ItemResponse second) {
    try {
      boolean sameName = StringUtils.equalsIgnoreCase(first.getText(), second.getText());
      boolean sameLevel = NumberUtils.compare(first.getItemLevel(), second.getItemLevel()) == 0;
      boolean sameLegMods = StringUtils
          .equalsIgnoreCase(first.getItemDetails().getAbbreviation(), second.getItemDetails().getAbbreviation());
      return sameName && sameLevel && sameLegMods;
    } catch (Exception ignored) {
      return false;
    }
  }

  public static String toCSV(List<?> list) {
    try {
      String json = AppConfig.getObjectMapper().writeValueAsString(list);
      JFlatCustom jFlat = new JFlatCustom(json);
      StringWriter stringWriter = new StringWriter();
      jFlat
          .json2Sheet()
          .headerSeparator(".")
          .write2csv(stringWriter, ',');
      return stringWriter.toString();
    } catch (Exception e) {
      log.error("Error converting to csv", e);
      return "";
    }
  }

  public static Number silentParse(String value) {
    try {
      return Double.valueOf(value);
    } catch (Exception ignored) {
    }
    return -1;
  }

}
