package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manson.domain.LegendaryMod;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.fo76.service.ItemSource;
import java.util.List;
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
public class ItemDetails {

  private String name;
  private String description;
  private ItemConfig config;
  private String abbreviation;
  private String abbreviationId;
  private ArmorGrade armorGrade;
  private List<LegendaryMod> legendaryMods;
  private ItemSource itemSource;
  private FilterFlag filterFlag;
  private List<Stats> stats;
  private OwnerInfo ownerInfo;
  private Double totalWeight;

}
