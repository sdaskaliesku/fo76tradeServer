package com.manson.fo76.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

@JsonIgnoreProperties(ignoreUnknown = true)
class LegendaryMod @JvmOverloads constructor(var value: String? = null, var star: Int = 0, var abbreviation: String? = null) {
    var id: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is LegendaryMod) {
            return false
        }
        val that = o
        return EqualsBuilder()
                .append(star, that.star)
                .append(value, that.value)
                .append(abbreviation, that.abbreviation)
                .append(id, that.id)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(value)
                .append(star)
                .append(abbreviation)
                .append(id)
                .toHashCode()
    }
}