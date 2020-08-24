package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.manson.fo76.domain.items.enums.FilterFlag;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {

  @Id
  private String id;
  @Indexed
  private String ownerId;
  @Indexed
  private String ownerName;
  @Indexed
  private String description;
  private Double price;
  @Indexed
  private Boolean tradeOnly;
  @Indexed
  private Boolean tradePossible;
  @Indexed
  private String text;
  @Indexed(unique = true)
  @JsonProperty(access = Access.WRITE_ONLY)
  private Long serverHandleId;
  private Integer count;
  private Integer itemValue;
  @Indexed
  private FilterFlag filterFlag;
  private Integer currentHealth;
  private Integer damage;
  private Integer durability;
  private Integer maximumHealth;
  private Double weight;
  private Double weaponDisplayAccuracy;
  private Double weaponDisplayRateOfFire;
  private Double weaponDisplayRange;
  @Indexed
  private Integer numLegendaryStars;
  @Indexed
  private Integer itemLevel;
  private Integer rarity;
  private Boolean isTradable;
  private Boolean isSpoiled;
  private Boolean isSetItem;
  private Boolean isQuestItem;
  @Indexed
  private Boolean isLegendary;
  @Indexed
  private List<StatsDTO> stats;
  @Indexed
  private List<LegendaryMod> legendaryMods;
  // TODO: Remove this, once final UI will be ready
  private List<String> legendaryModsTemp;

  public List<String> getLegendaryModsTemp() {
    return legendaryModsTemp;
  }

  public void setLegendaryModsTemp(List<String> legendaryModsTemp) {
    this.legendaryModsTemp = legendaryModsTemp;
  }

  public List<LegendaryMod> getLegendaryMods() {
    return legendaryMods;
  }

  public void setLegendaryMods(List<LegendaryMod> legendaryMods) {
    this.legendaryMods = legendaryMods;
  }

  public List<StatsDTO> getStats() {
    return stats;
  }

  public void setStats(List<StatsDTO> stats) {
    this.stats = stats;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
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

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getServerHandleId() {
    return serverHandleId;
  }

  public void setServerHandleId(Long serverHandleId) {
    this.serverHandleId = serverHandleId;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getItemValue() {
    return itemValue;
  }

  public void setItemValue(Integer itemValue) {
    this.itemValue = itemValue;
  }

  public FilterFlag getFilterFlag() {
    return filterFlag;
  }

  public void setFilterFlag(FilterFlag filterFlag) {
    this.filterFlag = filterFlag;
  }

  public Integer getCurrentHealth() {
    return currentHealth;
  }

  public void setCurrentHealth(Integer currentHealth) {
    this.currentHealth = currentHealth;
  }

  public Integer getDamage() {
    return damage;
  }

  public void setDamage(Integer damage) {
    this.damage = damage;
  }

  public Integer getDurability() {
    return durability;
  }

  public void setDurability(Integer durability) {
    this.durability = durability;
  }

  public Integer getMaximumHealth() {
    return maximumHealth;
  }

  public void setMaximumHealth(Integer maximumHealth) {
    this.maximumHealth = maximumHealth;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public Double getWeaponDisplayAccuracy() {
    return weaponDisplayAccuracy;
  }

  public void setWeaponDisplayAccuracy(Double weaponDisplayAccuracy) {
    this.weaponDisplayAccuracy = weaponDisplayAccuracy;
  }

  public Double getWeaponDisplayRateOfFire() {
    return weaponDisplayRateOfFire;
  }

  public void setWeaponDisplayRateOfFire(Double weaponDisplayRateOfFire) {
    this.weaponDisplayRateOfFire = weaponDisplayRateOfFire;
  }

  public Double getWeaponDisplayRange() {
    return weaponDisplayRange;
  }

  public void setWeaponDisplayRange(Double weaponDisplayRange) {
    this.weaponDisplayRange = weaponDisplayRange;
  }

  public Integer getNumLegendaryStars() {
    return numLegendaryStars;
  }

  public void setNumLegendaryStars(Integer numLegendaryStars) {
    this.numLegendaryStars = numLegendaryStars;
  }

  public Integer getItemLevel() {
    return itemLevel;
  }

  public void setItemLevel(Integer itemLevel) {
    this.itemLevel = itemLevel;
  }

  public Integer getRarity() {
    return rarity;
  }

  public void setRarity(Integer rarity) {
    this.rarity = rarity;
  }

  public Boolean getTradable() {
    return isTradable;
  }

  public void setTradable(Boolean tradable) {
    isTradable = tradable;
  }

  public Boolean getSpoiled() {
    return isSpoiled;
  }

  public void setSpoiled(Boolean spoiled) {
    isSpoiled = spoiled;
  }

  public Boolean getSetItem() {
    return isSetItem;
  }

  public void setSetItem(Boolean setItem) {
    isSetItem = setItem;
  }

  public Boolean getQuestItem() {
    return isQuestItem;
  }

  public void setQuestItem(Boolean questItem) {
    isQuestItem = questItem;
  }

  public Boolean getLegendary() {
    return isLegendary;
  }

  public void setLegendary(Boolean legendary) {
    isLegendary = legendary;
  }
}
