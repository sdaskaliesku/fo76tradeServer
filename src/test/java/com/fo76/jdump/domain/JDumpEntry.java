package com.fo76.jdump.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.domain.AbstractObject;
import java.util.List;

public class JDumpEntry extends AbstractObject {

  @JsonProperty("Record Header")
  private RecordHeader recordHeader;

  @JsonProperty("EDID")
  private String edid;
  @JsonProperty("DURL")
  private String durl;
  @JsonProperty("XALG")
  private String xalg;
  @JsonProperty("FULL")
  private String full;

  @JsonProperty("DESC")
  private Object desc;

  @JsonProperty("Model")
  private Model model;

  @JsonProperty("INDX")
  private String indx;

//  @JsonProperty("DATA")
//  private Data data;

  @JsonProperty("MNAM")
  private List<FormId> mnam;

  @JsonProperty("LNAM")
  private FormId lnam;

  @JsonProperty("NAM1")
  private String nam1;

  @JsonProperty("NAM2")
  private Object nam2;

  @JsonProperty("NAM3")
  private List<FormId> nam3;

  @JsonProperty("OBST")
  private Object obst;

  @JsonProperty("ARTC")
  private FormId artc;

  @JsonProperty("DNAM")
  private FormId dnam;

  @JsonProperty("ENAM")
  private FormId enam;

  @JsonProperty("FNAM")
  private List<FormId> fnam;


}
