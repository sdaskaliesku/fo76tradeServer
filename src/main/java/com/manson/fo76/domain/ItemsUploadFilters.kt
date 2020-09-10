package com.manson.fo76.domain

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import java.util.*

class ItemsUploadFilters {
    var filterFlags: List<Int> = ArrayList()
    var legendaryOnly = false
    var tradableOnly = false
    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("filterFlags", filterFlags)
                .append("legendaryOnly", legendaryOnly)
                .toString()
    }
}