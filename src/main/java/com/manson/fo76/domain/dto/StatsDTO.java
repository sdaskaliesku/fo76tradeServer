package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manson.fo76.domain.items.enums.DamageType;
import com.manson.fo76.domain.items.enums.ItemCardText;
import org.springframework.data.mongodb.core.index.Indexed;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsDTO {

	@Indexed
	private String text;
	@Indexed
	private ItemCardText textDecoded;
	private String value;
	@Indexed
	private DamageType damageType;
	private Integer difference;
	private Integer diffRating;
	private Integer precision;
	private Integer duration;
	private Boolean showAsDescription;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ItemCardText getTextDecoded() {
		return textDecoded;
	}

	public void setTextDecoded(ItemCardText textDecoded) {
		this.textDecoded = textDecoded;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
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
}
