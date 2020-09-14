package com.manson.fo76.domain

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class ModDataRequest {
    var modData: ModData = ModData()
    var filters: ItemsUploadFilters = ItemsUploadFilters()
    var version: Double? = null
    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("modData", modData)
                .append("filters", filters)
                .append("version", version)
                .toString()
    }
}