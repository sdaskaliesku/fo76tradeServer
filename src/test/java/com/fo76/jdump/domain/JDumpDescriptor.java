package com.fo76.jdump.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class JDumpDescriptor {

  @JsonProperty("OMOD")
  private Map<String, JDumpEntry> omod;

  public Map<String, JDumpEntry> getOmod() {
    return omod;
  }

  public void setOmod(Map<String, JDumpEntry> omod) {
    this.omod = omod;
  }
}
