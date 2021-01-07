package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manson.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModDataRequest extends AbstractObject {

  private ModData modData;
  private ItemsUploadFilters filters;
  private Double version;

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

  public Double getVersion() {
    return version;
  }

  public void setVersion(Double version) {
    this.version = version;
  }
}
