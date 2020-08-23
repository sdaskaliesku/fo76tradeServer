package com.manson.fo76.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LegendaryMod {

  private int star;
  private String text;
  private String abbreviation;
  private Map<String, String> translations;
  private List<ItemType> itemTypes = new ArrayList<>();

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

  public List<ItemType> getItemTypes() {
    return itemTypes;
  }

  public void setItemTypes(List<ItemType> itemTypes) {
    this.itemTypes = itemTypes;
  }
}
