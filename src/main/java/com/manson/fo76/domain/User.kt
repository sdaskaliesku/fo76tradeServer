package com.manson.fo76.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

@JsonIgnoreProperties(ignoreUnknown = true)
class User {
    @Id
    var id: String? = null

    @Indexed(unique = true)
    var name: String? = null

    @Indexed(unique = true)
    var user: String? = null
    var password: String? = null
    var contactInfo: ContactInfo? = null
    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("id", id)
                .append("name", name)
                .toString()
    }
}