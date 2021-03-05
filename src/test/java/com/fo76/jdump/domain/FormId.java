package com.fo76.jdump.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.domain.AbstractObject;

public class FormId extends AbstractObject {

  @JsonProperty("formid")
  private String formId;
  private String signature;
  @JsonProperty("editorid")
  private String editorId;
  private String name;
  @JsonProperty("@type")
  private String type;

  public String getFormId() {
    return formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getEditorId() {
    return editorId;
  }

  public void setEditorId(String editorId) {
    this.editorId = editorId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
