package com.manson.fo76.service;

import com.manson.fo76.domain.AbstractObject;

public class RogueTraderConfig extends AbstractObject {
  private String branch;
  private String gameId;
  private String key;
  private Meta meta;

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Meta getMeta() {
    return meta;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }
}
