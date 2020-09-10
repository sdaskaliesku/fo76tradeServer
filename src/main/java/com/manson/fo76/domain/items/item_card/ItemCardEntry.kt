package com.manson.fo76.domain.items.item_card

import com.fasterxml.jackson.annotation.JsonIgnore
import com.manson.fo76.domain.AbstractObject
import com.manson.fo76.domain.items.enums.DamageType
import com.manson.fo76.domain.items.enums.DamageType.Companion.fromDamageType

class ItemCardEntry : AbstractObject() {
    var text: String? = null
    var value: String? = null
    var damageType: Int? = null
    var difference: Int? = null
    var diffRating: Int? = null
    var precision: Int? = null
    var duration: Int? = null
    var showAsDescription: Boolean? = null
    var components: List<ItemCardEntryComponent> = listOf()

    @get:JsonIgnore
    val damageTypeEnum: DamageType
        get() = fromDamageType(damageType!!)
}