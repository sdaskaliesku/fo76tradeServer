package com.manson.fo76.domain.dto

import com.manson.domain.fed76.pricing.PriceCheckResponse
import com.manson.domain.fo76.items.enums.ArmorGrade

class ItemDetails {
    var name: String = ""
    var fedName: String = ""
    var formId: String = ""
    var abbreviation: String = ""
    var armorGrade: ArmorGrade = ArmorGrade.Unknown
    var priceCheckResponse: PriceCheckResponse = PriceCheckResponse()
}