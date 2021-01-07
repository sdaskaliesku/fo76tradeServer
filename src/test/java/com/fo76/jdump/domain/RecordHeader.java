package com.fo76.jdump.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.AbstractObject;

public class RecordHeader extends AbstractObject {

  @JsonProperty("Signature")
  private String signature;
  @JsonProperty("Data Size")
  private String dataSize;

  // TODO: string or empty object?
  @JsonProperty("Record Flags")
  private Object recordFlags;

  @JsonProperty("FormID")
  private FormId formId;

  @JsonProperty("Version Control Info 1")
  private String versionControlInfo1;
  @JsonProperty("Form Version")
  private String formVersion;
  @JsonProperty("Version Control Info 2")
  private String versionControlInfo2;

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getDataSize() {
    return dataSize;
  }

  public void setDataSize(String dataSize) {
    this.dataSize = dataSize;
  }

  public Object getRecordFlags() {
    return recordFlags;
  }

  public void setRecordFlags(Object recordFlags) {
    this.recordFlags = recordFlags;
  }

  public FormId getFormId() {
    return formId;
  }

  public void setFormId(FormId formId) {
    this.formId = formId;
  }

  public String getVersionControlInfo1() {
    return versionControlInfo1;
  }

  public void setVersionControlInfo1(String versionControlInfo1) {
    this.versionControlInfo1 = versionControlInfo1;
  }

  public String getFormVersion() {
    return formVersion;
  }

  public void setFormVersion(String formVersion) {
    this.formVersion = formVersion;
  }

  public String getVersionControlInfo2() {
    return versionControlInfo2;
  }

  public void setVersionControlInfo2(String versionControlInfo2) {
    this.versionControlInfo2 = versionControlInfo2;
  }
}
