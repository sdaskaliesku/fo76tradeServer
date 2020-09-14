package com.manson.fo76.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Fo76String {
    @JsonProperty("EDID")
    var edid: String? = null

    @JsonProperty("REC")
    var rec: Object? = null

    @JsonProperty("Source")
    var source: String? = null

    @JsonProperty("Dest")
    var dest: String? = null

    @JsonProperty("-List")
    var list: String? = null

    @JsonProperty("-sID")
    var sid: String? = null
    var lang: String = "en"
}