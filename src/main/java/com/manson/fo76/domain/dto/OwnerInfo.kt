package com.manson.fo76.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.springframework.data.mongodb.core.index.Indexed

@JsonIgnoreProperties(ignoreUnknown = true)
class OwnerInfo {
    @Indexed
    var id: String? = null

    @Indexed
    var name: String? = null

    @Indexed
    var accountOwner: String? = null

    @Indexed
    var characterOwner: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is OwnerInfo) {
            return false
        }
        val ownerInfo = o
        return EqualsBuilder()
                .append(id, ownerInfo.id)
                .append(name, ownerInfo.name)
                .append(accountOwner, ownerInfo.accountOwner)
                .append(characterOwner, ownerInfo.characterOwner)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(accountOwner)
                .append(characterOwner)
                .toHashCode()
    }
}