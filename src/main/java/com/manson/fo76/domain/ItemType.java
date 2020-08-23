package com.manson.fo76.domain;

public enum ItemType {
  melee("melee"), armor("armor"), ranged("ranged");

  private final String value;

  ItemType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
