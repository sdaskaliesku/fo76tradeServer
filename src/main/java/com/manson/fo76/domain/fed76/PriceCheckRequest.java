package com.manson.fo76.domain.fed76;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import com.manson.fo76.domain.dto.FilterFlag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PriceCheckRequest {

  private List<String> ids;
  private String item;
  private String mods;
  @Builder.Default
  private ArmorGrade grade = ArmorGrade.Unknown;
  private String gradeId;
  @Builder.Default
  private FilterFlag filterFlag = FilterFlag.UNKNOWN;

  @JsonIgnore
  public boolean isValid() {
    if (FilterFlag.NOTES == filterFlag) {
      return StringUtils.isNoneBlank(item);
    }
    return StringUtils.isNoneBlank(item, mods) || CollectionUtils.isNotEmpty(ids);
  }

  @JsonIgnore
  public String toId() {
    return String.format("%s/%s/%s/%s", item, mods, grade.getValue(), filterFlag.getValue());
  }

}
