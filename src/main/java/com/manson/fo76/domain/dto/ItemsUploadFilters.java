package com.manson.fo76.domain.dto;

import com.manson.AbstractObject;
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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemsUploadFilters extends AbstractObject {

  private List<Long> filterFlags;
  private boolean legendaryOnly = false;
  private boolean tradableOnly = false;
  private boolean priceCheckOnly = false;

}
