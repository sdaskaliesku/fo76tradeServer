package com.manson.fo76.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.manson.fo76.domain.items.enums.DamageType
import com.manson.fo76.domain.items.enums.ItemCardText
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.springframework.data.mongodb.core.index.Indexed

@JsonIgnoreProperties(ignoreUnknown = true)
class StatsDTO {
    @Indexed
    var text: ItemCardText = ItemCardText.UNKNOWN
    var value: String? = null

    @Indexed
    var damageType: DamageType? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is StatsDTO) {
            return false
        }
        val statsDTO = o
        return EqualsBuilder()
                .append(text, statsDTO.text)
                .append(value, statsDTO.value)
                .append(damageType, statsDTO.damageType)
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