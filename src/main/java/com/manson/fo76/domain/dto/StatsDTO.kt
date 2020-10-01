package com.manson.fo76.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.manson.domain.fo76.items.enums.DamageType
import com.manson.domain.fo76.items.enums.ItemCardText
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.springframework.data.mongodb.core.index.Indexed

@JsonIgnoreProperties(ignoreUnknown = true)
class StatsDTO {
    @Indexed
    var text: ItemCardText = ItemCardText.UNKNOWN
    var value: String? = null

    @Indexed
    var damageType: DamageType = DamageType.RADIATION
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is StatsDTO) {
            return false
        }
        return EqualsBuilder()
                .append(text, other.text)
                .append(value, other.value)
                .append(damageType, other.damageType)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(text)
                .append(value)
                .append(damageType)
                .toHashCode()
    }
}