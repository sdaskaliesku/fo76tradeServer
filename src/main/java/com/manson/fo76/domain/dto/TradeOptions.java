package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.index.Indexed;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeOptions {

  @Indexed
  private String description;
  private Double price;
  @Indexed
  private Boolean tradeOnly;
  @Indexed
  private Boolean tradePossible;
  private Double gamePrice;
  private Double vendorPrice;

  public Double getGamePrice() {
    return gamePrice;
  }

  public void setGamePrice(Double gamePrice) {
    this.gamePrice = gamePrice;
  }

  public Double getVendorPrice() {
    return vendorPrice;
  }

  public void setVendorPrice(Double vendorPrice) {
    this.vendorPrice = vendorPrice;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Boolean getTradeOnly() {
    return tradeOnly;
  }

  public void setTradeOnly(Boolean tradeOnly) {
    this.tradeOnly = tradeOnly;
  }

  public Boolean getTradePossible() {
    return tradePossible;
  }

  public void setTradePossible(Boolean tradePossible) {
    this.tradePossible = tradePossible;
  }
}
