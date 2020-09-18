package com.manson.fo76.domain.fed76.mapping

import com.fasterxml.jackson.annotation.JsonProperty

class Fed76ApiMapping {
    @JsonProperty("by_id")
    var byId: MutableMap<String, Fed76ApiMappingEntry> = HashMap()

    @JsonProperty("by_name")
    var byName: MutableMap<String, Fed76ApiMappingEntry> = HashMap()
}