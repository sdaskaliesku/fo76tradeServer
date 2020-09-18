package com.manson.fo76.domain.fed76.mapping

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.apache.commons.lang3.StringUtils

@JsonIgnoreProperties(ignoreUnknown = true)
class Fed76ApiMappingEntry {

    @JsonUnwrapped
    var id: String? = ""
    @JsonUnwrapped
    var name: String? = ""
    @JsonUnwrapped
    var queries: List<String> = listOf()
    @JsonUnwrapped
    var ids: List<String> = listOf()

    fun retrieveId(): String {
        if (StringUtils.isBlank(id)) {
            return name.toString()
        }
        if (StringUtils.isBlank(name)) {
            return id.toString()
        }
        return ""
    }
}