package com.fo76.jdump.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.AbstractObject;

public class Model extends AbstractObject {

  @JsonProperty("ENLS")
  private String enls;

  @JsonProperty("ENLT")
  private Enlt enlt;

  @JsonProperty("AUUV")
  private Auuv auuv;

}
