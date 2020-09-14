package com.manson.fo76.domain.dto

import com.manson.fo76.domain.items.enums.ArmorGrade
import com.manson.fo76.domain.pricing.PriceCheckResponse

class ItemDetails {
    var name: String = ""
    var fedName: String = ""
    var formId: String = ""
    var abbreviation: String = ""
    var armorGrade: ArmorGrade = ArmorGrade.Unknown
    var priceCheckResponse: PriceCheckResponse = PriceCheckResponse()
}