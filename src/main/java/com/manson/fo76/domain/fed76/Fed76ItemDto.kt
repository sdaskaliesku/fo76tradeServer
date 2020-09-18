package com.manson.fo76.domain.fed76

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.manson.fo76.domain.dto.LegendaryMod
import com.manson.fo76.domain.items.enums.FilterFlag

@JsonIgnoreProperties(ignoreUnknown = true)
class Fed76ItemDto {
    var tradeOptions: Fed76TradeOptions = Fed76TradeOptions()
    var itemDetails: Fed76ItemDetails = Fed76ItemDetails()
    var text: String? = null
    var filterFlag: FilterFlag = FilterFlag.UNKNOWN
    var itemLevel: Int = 0
    var isTradable: Boolean = false
    var isLegendary: Boolean = false
    var legendaryMods: List<LegendaryMod> = listOf()
}