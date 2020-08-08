package com.manson.fo76.domain.items.item_card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manson.fo76.domain.AbstractObject;
import com.manson.fo76.domain.items.enums.DamageType;
import com.manson.fo76.domain.items.enums.ItemCardText;
import java.util.List;

public class ItemCardEntry extends AbstractObject {

	private String text;
	private String value;
	private Integer damageType;
	private Integer difference;
	private Integer diffRating;
	private Integer precision;
	private Integer duration;
	private Boolean showAsDescription;
	private List<ItemCardEntryComponent> components;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getDamageType() {
		return damageType;
	}

	public void setDamageType(Integer damageType) {
		this.damageType = damageType;
	}

	public Integer getDifference() {
		return difference;
	}

	public void setDifference(Integer difference) {
		this.difference = difference;
	}

	public Integer getDiffRating() {
		return diffRating;
	}

	public void setDiffRating(Integer diffRating) {
		this.diffRating = diffRating;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Boolean getShowAsDescription() {
		return showAsDescription;
	}

	public void setShowAsDescription(Boolean showAsDescription) {
		this.showAsDescription = showAsDescription;
	}

	public List<ItemCardEntryComponent> getComponents() {
		return components;
	}

	public void setComponents(List<ItemCardEntryComponent> components) {
		this.components = components;
	}

	@JsonIgnore
	public ItemCardText getItemCardText() {
		return ItemCardText.fromString(text);
	}

	@JsonIgnore
	public DamageType getDamageTypeEnum() {
		return DamageType.fromDamageType(damageType);
	}
}
