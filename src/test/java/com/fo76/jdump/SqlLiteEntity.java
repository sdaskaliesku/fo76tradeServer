package com.fo76.jdump;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "fo76jdump")
public class SqlLiteEntity {

  @Id
  private String formid;
  private String signature;
  private String editorid;
  private String name;
  private String desc;
  private String flags;
  private String version;
  private String versioncontrol;
  private String jdump;

  public String getFormid() {
    return formid;
  }

  public void setFormid(String formid) {
    this.formid = formid;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getEditorid() {
    return editorid;
  }

  public void setEditorid(String editorid) {
    this.editorid = editorid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getJdump() {
    return jdump;
  }

  public void setJdump(String jdump) {
    this.jdump = jdump;
  }

  public String getFlags() {
    return flags;
  }

  public void setFlags(String flags) {
    this.flags = flags;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersioncontrol() {
    return versioncontrol;
  }

  public void setVersioncontrol(String versioncontrol) {
    this.versioncontrol = versioncontrol;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
        .append("formid", formid)
        .append("signature", signature)
        .append("editorid", editorid)
        .append("name", name)
        .append("desc", desc)
//        .append("flags", flags)
//        .append("version", version)
//        .append("versioncontrol", versioncontrol)
        .toString();
  }
}
