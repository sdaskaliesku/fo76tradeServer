package com.manson.fo76.domain.dto

import com.manson.fo76.domain.items.enums.ArmorGrade

class ItemDetails {
    var name: String = ""
    var formId: String = ""
    var abbreviation: String = ""
    var armorGrade: ArmorGrade = ArmorGrade.Unknown
}