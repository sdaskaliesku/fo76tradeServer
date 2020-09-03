package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.index.Indexed;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OwnerInfo {

  @Indexed
  private String id;
  @Indexed
  private String name;
  @Indexed
  private String accountOwner;
  @Indexed
  private String characterOwner;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAccountOwner() {
    return accountOwner;
  }

  public void setAccountOwner(String accountOwner) {
    this.accountOwner = accountOwner;
  }

  public String getCharacterOwner() {
    return characterOwner;
  }

  public void setCharacterOwner(String characterOwner) {
    this.characterOwner = characterOwner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof OwnerInfo)) {
      return false;
    }

    OwnerInfo ownerInfo = (OwnerInfo) o;

    return new EqualsBuilder()
        .append(id, ownerInfo.id)
        .append(name, ownerInfo.name)
        .append(accountOwner, ownerInfo.accountOwner)
        .append(characterOwner, ownerInfo.characterOwner)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(name)
        .append(accountOwner)
        .append(characterOwner)
        .toHashCode();
  }
}
