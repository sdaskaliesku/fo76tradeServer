package com.manson.fo76.domain;

import com.manson.fo76.CharacterInventory;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ModData extends AbstractObject {

  private ModUser user;
  private Map<String, CharacterInventory> characterInventories;

  public Map<String, CharacterInventory> getCharacterInventories() {
    return characterInventories;
  }

  public void setCharacterInventories(Map<String, CharacterInventory> characterInventories) {
    this.characterInventories = characterInventories;
  }

  public ModUser getUser() {
    return user;
  }

  public void setUser(ModUser user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE)
        .append("characterInventories", characterInventories)
        .append("user", user)
        .toString();
  }
}
