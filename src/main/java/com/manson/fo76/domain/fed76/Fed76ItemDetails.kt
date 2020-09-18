package com.manson.fo76.domain.fed76

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.manson.fo76.domain.items.enums.ArmorGrade

@JsonIgnoreProperties(ignoreUnknown = true)
class Fed76ItemDetails {
    var name: String = ""
    var fedName: String = ""
    var formId: String = ""
    var abbreviation: String = ""
    var armorGrade: ArmorGrade = ArmorGrade.Unknown
}