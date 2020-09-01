package com.manson.fo76.domain;

public class CharacterInfoData extends AbstractObject {

  private String name;
  private Integer level;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }
}
