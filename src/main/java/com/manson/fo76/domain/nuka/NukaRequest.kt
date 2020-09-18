package com.manson.fo76.domain.nuka

class NukaRequest {
    var searchtext = ""
    var searchmatches: MutableMap<String, String> = HashMap()
    var currentPage = 0
    var prevPage = 0
    var prevNext = "start"

    public fun toMultiPartMap(): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        map["searchtext"] = searchtext
        map["currentPage"] = currentPage.toString()
        map["prevPage"] = prevPage.toString()
        map["prevNext"] = prevNext
        map["searchmatches[name]"] = "on"
        map["searchmatches[EDID]"] = "on"
        return map
    }
}