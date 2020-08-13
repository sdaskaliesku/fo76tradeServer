package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manson.fo76.domain.items.enums.DamageType;
import com.manson.fo76.domain.items.enums.ItemCardText;
import org.springframework.data.mongodb.core.index.Indexed;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsDTO {

	@Indexed
	private ItemCardText text;
	private String value;
	@Indexed
	private DamageType damageType;

	public ItemCardText getText() {
		return text;
	}

	public void setText(ItemCardText text) {
		this.text = text;
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

}
