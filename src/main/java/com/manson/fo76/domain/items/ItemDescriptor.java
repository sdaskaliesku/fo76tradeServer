package com.manson.fo76.domain.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.fo76.domain.AbstractObject;
import com.manson.fo76.domain.items.enums.FilterFlag;
import com.manson.fo76.domain.items.item_card.ItemCardEntry;
import com.manson.fo76.domain.items.mod_card.ModCardEntry;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ItemDescriptor extends AbstractObject {

	private String text;
	private Long serverHandleId;
	private Integer count;
	private Integer itemValue;
	private Integer filterFlag;
	private Integer currentHealth;
	private Integer damage;
	private Integer durability;
	private Integer maximumHealth;
	private Double weight;
	private Double weaponDisplayAccuracy;
	private Double weaponDisplayRateOfFire;
	private Double weaponDisplayRange;
	private Integer numLegendaryStars;
	private Integer itemLevel;
	private Integer rarity;
	private Boolean isTradable;
	private Boolean isSpoiled;
	private Boolean isSetItem;
	private Boolean isQuestItem;
	private Boolean isLegendary;
	@JsonProperty("ItemCardEntries")
	private List<ItemCardEntry> itemCardEntries;
	private VendingData vendingData;
	@JsonProperty("ModCardEntries")
	private List<ModCardEntry> modCardEntries;

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

	public Integer getFilterFlag() {
		return filterFlag;
	}

	public void setFilterFlag(Integer filterFlag) {
		this.filterFlag = filterFlag;
	}

	public Integer getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(Integer currentHealth) {
		this.currentHealth = currentHealth;
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

	public List<ItemCardEntry> getItemCardEntries() {
		return itemCardEntries;
	}

	public void setItemCardEntries(List<ItemCardEntry> itemCardEntries) {
		this.itemCardEntries = itemCardEntries;
	}

	public VendingData getVendingData() {
		return vendingData;
	}

	public void setVendingData(VendingData vendingData) {
		this.vendingData = vendingData;
	}

	public List<ModCardEntry> getModCardEntries() {
		return modCardEntries;
	}

	public void setModCardEntries(List<ModCardEntry> modCardEntries) {
		this.modCardEntries = modCardEntries;
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

	@JsonIgnore
	public FilterFlag getFilterFlagEnum() {
		return FilterFlag.fromInt(filterFlag);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ItemDescriptor that = (ItemDescriptor) o;

		return new EqualsBuilder()
				.append(text, that.text)
				.append(serverHandleId, that.serverHandleId)
				.append(count, that.count)
				.append(itemValue, that.itemValue)
				.append(filterFlag, that.filterFlag)
				.append(currentHealth, that.currentHealth)
				.append(damage, that.damage)
				.append(durability, that.durability)
				.append(maximumHealth, that.maximumHealth)
				.append(weight, that.weight)
				.append(weaponDisplayAccuracy, that.weaponDisplayAccuracy)
				.append(weaponDisplayRateOfFire, that.weaponDisplayRateOfFire)
				.append(weaponDisplayRange, that.weaponDisplayRange)
				.append(numLegendaryStars, that.numLegendaryStars)
				.append(itemLevel, that.itemLevel)
				.append(rarity, that.rarity)
				.append(isTradable, that.isTradable)
				.append(isSpoiled, that.isSpoiled)
				.append(isSetItem, that.isSetItem)
				.append(isQuestItem, that.isQuestItem)
				.append(isLegendary, that.isLegendary)
				.append(itemCardEntries, that.itemCardEntries)
				.append(vendingData, that.vendingData)
				.append(modCardEntries, that.modCardEntries)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(text)
				.append(serverHandleId)
				.append(count)
				.append(itemValue)
				.append(filterFlag)
				.append(currentHealth)
				.append(damage)
				.append(durability)
				.append(maximumHealth)
				.append(weight)
				.append(weaponDisplayAccuracy)
				.append(weaponDisplayRateOfFire)
				.append(weaponDisplayRange)
				.append(numLegendaryStars)
				.append(itemLevel)
				.append(rarity)
				.append(isTradable)
				.append(isSpoiled)
				.append(isSetItem)
				.append(isQuestItem)
				.append(isLegendary)
				.append(itemCardEntries)
				.append(vendingData)
				.append(modCardEntries)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("text", text)
				.append("serverHandleId", serverHandleId)
				.append("count", count)
				.append("itemValue", itemValue)
				.append("filterFlag", filterFlag)
				.append("numLegendaryStars", numLegendaryStars)
				.append("itemLevel", itemLevel)
				.toString();
	}
}
