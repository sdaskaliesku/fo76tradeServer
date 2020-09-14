package com.manson.fo76.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.mongodb.core.index.Indexed

@JsonIgnoreProperties(ignoreUnknown = true)
class TradeOptions {
    @Indexed
    var description: String? = null
    var price: Double? = null

    @Indexed
    var tradeOnly: Boolean? = null

    @Indexed
    var tradePossible: Boolean? = null
    var gamePrice: Double? = null
    var vendorPrice: Double? = null
}