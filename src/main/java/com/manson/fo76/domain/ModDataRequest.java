package com.manson.fo76.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ModDataRequest {

  private ModData modData;
  private ItemsUploadFilters filters;

  public ModData getModData() {
    return modData;
  }

  public void setModData(ModData modData) {
    this.modData = modData;
  }

  public ItemsUploadFilters getFilters() {
    return filters;
  }

  public void setFilters(ItemsUploadFilters filters) {
    this.filters = filters;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE)
        .append("modData", modData)
        .append("filters", filters)
        .toString();
  }
}
