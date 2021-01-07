package com.manson.fo76.domain.fed76;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PlanReviewResponse extends AbstractObject {

  @JsonProperty("drop_rate")
  private Integer dropRate;
  private Integer utility;
  private Integer farmability;
  @JsonProperty("vendor_min")
  private Integer vendorMin;
  @JsonProperty("vendor_max")
  private Integer vendorMax;

}
