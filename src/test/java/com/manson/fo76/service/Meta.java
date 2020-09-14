package com.manson.fo76.service;

import com.manson.fo76.domain.AbstractObject;
import java.util.List;

public class Meta extends AbstractObject {
  private Is is;
  private List<String> lookup;
  private String name;
  private int position;

  public Is getIs() {
    return is;
  }

  public void setIs(Is is) {
    this.is = is;
  }

  public List<String> getLookup() {
    return lookup;
  }

  public void setLookup(List<String> lookup) {
    this.lookup = lookup;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
}
