package com.manson.fo76;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.fo76.domain.AccountInfoData;
import com.manson.fo76.domain.CharacterInfoData;
import com.manson.fo76.domain.items.ItemDescriptor;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CharacterInventory {

  private List<ItemDescriptor> playerInventory;
  private List<ItemDescriptor> stashInventory;
  @JsonProperty("AccountInfoData")
  private AccountInfoData accountInfoData;
  @JsonProperty("CharacterInfoData")
  private CharacterInfoData characterInfoData;

  public List<ItemDescriptor> getPlayerInventory() {
    return playerInventory;
  }

  public void setPlayerInventory(List<ItemDescriptor> playerInventory) {
    this.playerInventory = playerInventory;
  }

  public List<ItemDescriptor> getStashInventory() {
    return stashInventory;
  }

  public void setStashInventory(List<ItemDescriptor> stashInventory) {
    this.stashInventory = stashInventory;
  }

  public AccountInfoData getAccountInfoData() {
    return accountInfoData;
  }

  public void setAccountInfoData(AccountInfoData accountInfoData) {
    this.accountInfoData = accountInfoData;
  }

  public CharacterInfoData getCharacterInfoData() {
    return characterInfoData;
  }

  public void setCharacterInfoData(CharacterInfoData characterInfoData) {
    this.characterInfoData = characterInfoData;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
        .append("playerInventory", playerInventory)
        .append("stashInventory", stashInventory)
        .append("accountInfoData", accountInfoData)
        .append("characterInfoData", characterInfoData)
        .toString();
  }
}
