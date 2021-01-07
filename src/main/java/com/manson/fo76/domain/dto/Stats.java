package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manson.AbstractObject;
import com.manson.domain.fo76.items.enums.DamageType;
import com.manson.domain.fo76.items.enums.ItemCardText;
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
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {

  private ItemCardText itemCardText;
  private String value;
  private DamageType damageType;

}
