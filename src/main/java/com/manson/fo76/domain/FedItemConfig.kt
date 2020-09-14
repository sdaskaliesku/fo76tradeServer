package com.manson.fo76.domain

import com.manson.fo76.domain.items.enums.FilterFlag

class FedItemConfig {
    var type: FilterFlag = FilterFlag.UNKNOWN
    var text: String = ""
    var abbreviation: String = ""
}