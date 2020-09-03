package com.manson.fo76.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manson.fo76.domain.items.enums.DamageType;
import com.manson.fo76.domain.items.enums.ItemCardText;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof StatsDTO)) {
			return false;
		}

		StatsDTO statsDTO = (StatsDTO) o;

		return new EqualsBuilder()
				.append(text, statsDTO.text)
				.append(value, statsDTO.value)
				.append(damageType, statsDTO.damageType)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(text)
				.append(value)
				.append(damageType)
				.toHashCode();
	}
}
