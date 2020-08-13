package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LegendaryMod {

  private String value;
  private int star;

  public LegendaryMod(String value, int star) {
    this.value = value;
    this.star = star;
  }

  public LegendaryMod(String value) {
    this(value, 0);
  }

  public LegendaryMod() {
    this(null);
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getStar() {
    return star;
  }

  public void setStar(int star) {
    this.star = star;
  }
}
