package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LegendaryMod {

  private String value;
  private int star;
  private String abbreviation;
  private String id;

  public LegendaryMod(String value, int star, String abbreviation) {
    this.value = value;
    this.star = star;
    this.abbreviation = abbreviation;
  }

  public LegendaryMod(String value) {
    this(value, 0, null);
  }

  public LegendaryMod() {
    this(null);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public String getAbbreviation() {
    return abbreviation;
  }

  public void setAbbreviation(String abbreviation) {
    this.abbreviation = abbreviation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LegendaryMod)) {
      return false;
    }

    LegendaryMod that = (LegendaryMod) o;

    return new EqualsBuilder()
        .append(star, that.star)
        .append(value, that.value)
        .append(abbreviation, that.abbreviation)
        .append(id, that.id)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(value)
        .append(star)
        .append(abbreviation)
        .append(id)
        .toHashCode();
  }
}
