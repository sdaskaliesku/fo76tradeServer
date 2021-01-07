package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.AbstractObject;
import com.manson.domain.fo76.AccountInfoData;
import com.manson.domain.fo76.CharacterInfoData;
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
public class CharacterInventory extends AbstractObject {

  private List<ItemDescriptor> playerInventory;
  private List<ItemDescriptor> stashInventory;
  @JsonProperty("AccountInfoData")
  private AccountInfoData accountInfoData;
  @JsonProperty("CharacterInfoData")
  private CharacterInfoData characterInfoData;

}
