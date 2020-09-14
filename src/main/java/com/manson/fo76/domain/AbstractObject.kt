package com.manson.fo76.domain

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import java.util.*

open class AbstractObject {
    protected var unknownFields: MutableMap<String, Any> = HashMap()

    @JsonAnyGetter
    fun getUnknownFieldsMap(): Map<String, Any> {
        return unknownFields
    }

    @JsonAnySetter
    fun setUnknownField(key: String, value: Any) {
        unknownFields[key] = value
    }
}