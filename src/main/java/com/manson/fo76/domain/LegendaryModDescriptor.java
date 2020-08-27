package com.manson.fo76.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LegendaryModDescriptor {

  private int star;
  private String text;
  private String abbreviation;
  private List<String> additionalAbbreviations;
  private String id;
  private Map<String, String> translations;
  private String itemType;
  private boolean enabled = true;

  public LegendaryModDescriptor() {
    translations = new HashMap<>();
    additionalAbbreviations = new ArrayList<>();
  }

  public Map<String, String> getTranslations() {
    return translations;
  }

  public void setTranslations(Map<String, String> translations) {
    this.translations = translations;
  }

  public int getStar() {
    return star;
  }

  public void setStar(int star) {
    this.star = star;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  public List<String> getAdditionalAbbreviations() {
    return additionalAbbreviations;
  }

  public void setAdditionalAbbreviations(List<String> additionalAbbreviations) {
    this.additionalAbbreviations = additionalAbbreviations;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getItemType() {
    return itemType;
  }

  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  private static String prepareString(String input) {
    return input.trim().replace("'", "").replace("+", "").replace(".", "");
  }

  @JsonIgnore
  public boolean isTheSameMod(String input) {
    input = prepareString(input);
    boolean matchesName = StringUtils.equalsIgnoreCase(prepareString(text), input);
    if (matchesName) {
      return true;
    }
    for (String translation: translations.values()) {
      if (StringUtils.equalsIgnoreCase(prepareString(translation), input)) {
        return true;
      }
    }
    return false;
  }
}
