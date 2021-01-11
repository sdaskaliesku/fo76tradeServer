package com.manson.fo76.domain.fed76;

import com.manson.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BasePriceCheckResponse extends AbstractObject {

  private String name;
  private Integer price;
  private String category;
  private String timestamp;
  private String path;
  private String description;

}
