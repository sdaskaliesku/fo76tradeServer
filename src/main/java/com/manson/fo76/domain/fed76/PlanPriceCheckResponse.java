package com.manson.fo76.domain.fed76;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PlanPriceCheckResponse extends BasePriceCheckResponse {

  @JsonProperty("plan_class")
  private String planClass;
  @JsonProperty("plan_subclass")
  private String planSubClass;
  private Boolean tradeable;
  @JsonProperty("plan_type")
  private String planType;
  private String formId;

  private Object review;

}
