package com.fo76.jdump.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.domain.AbstractObject;

public class Enlt extends AbstractObject {
  @JsonProperty("Red")
  private String red;
  @JsonProperty("Green")
  private String green;
  @JsonProperty("Blue")
  private String blue;
  @JsonProperty("Alpha")
  private String alpha;

}
