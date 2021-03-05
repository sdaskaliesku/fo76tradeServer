package com.manson.fo76.web.api;

import com.manson.domain.fo76.items.enums.FilterFlag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

@Data
public class FilterFlagResponse {

  private final String name;
  private final List<Long> flags;
  private final String value;
  private final boolean hasStarMods;
  private final List<FilterFlagResponse> subtypes;
  private final boolean parent;

  public FilterFlagResponse(FilterFlag filterFlag) {
    this.value = filterFlag.getValue();
    this.flags = filterFlag.getFlags();
    this.hasStarMods = filterFlag.isHasStarMods();
    this.subtypes = filterFlag.getSubtypes().stream().map(FilterFlagResponse::new).collect(Collectors.toList());
    this.name = filterFlag.name();
    this.parent = filterFlag.isParent();
  }

}