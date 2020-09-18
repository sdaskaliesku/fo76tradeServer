package com.manson.fo76.domain.fed76

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Fed76TradeOptions {
    var gamePrice: Double? = null
    var vendorPrice: Double? = null
}