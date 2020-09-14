package com.manson.fo76.domain.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.manson.fo76.domain.AbstractObject
import com.manson.fo76.domain.dto.OwnerInfo
import com.manson.fo76.domain.items.enums.FilterFlag
import com.manson.fo76.domain.items.enums.FilterFlag.Companion.fromInt
import com.manson.fo76.domain.items.item_card.ItemCardEntry
import com.manson.fo76.domain.items.mod_card.ModCardEntry
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class ItemDescriptor : AbstractObject() {
    var ownerInfo: OwnerInfo? = null
    var text: String? = null
    var serverHandleId: Long? = null
    var count: Int? = null
    var itemValue: Int = 0
    var filterFlag: Int? = null
    var currentHealth: Int? = null
    var damage: Int? = null
    var durability: Int? = null
    var maximumHealth: Int? = null
    var weight: Double? = null
    var weaponDisplayAccuracy: Double? = null
    var weaponDisplayRateOfFire: Double? = null
    var weaponDisplayRange: Double? = null
    var numLegendaryStars: Int = 0
    var itemLevel: Int? = null
    var rarity: Int? = null

    var isTradable: Boolean = false
    var isSpoiled: Boolean = false
    var isSetItem: Boolean = false
    var isQuestItem: Boolean = false
    var isLegendary: Boolean = false

    @JsonProperty("ItemCardEntries")
    var itemCardEntries: List<ItemCardEntry>? = null
    var vendingData: VendingData? = null

    @JsonProperty("ModCardEntries")
    var modCardEntries: List<ModCardEntry>? = null

    @get:JsonIgnore
    val filterFlagEnum: FilterFlag
        get() = fromInt(filterFlag)



    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("text", text)
                .append("serverHandleId", serverHandleId)
                .append("count", count)
                .append("itemValue", itemValue)
                .append("filterFlag", filterFlag)
                .append("numLegendaryStars", numLegendaryStars)
                .append("itemLevel", itemLevel)
                .toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemDescriptor) return false

        if (ownerInfo != other.ownerInfo) return false
        if (text != other.text) return false
        if (serverHandleId != other.serverHandleId) return false
        if (count != other.count) return false
        if (itemValue != other.itemValue) return false
        if (filterFlag != other.filterFlag) return false
        if (currentHealth != other.currentHealth) return false
        if (damage != other.damage) return false
        if (durability != other.durability) return false
        if (maximumHealth != other.maximumHealth) return false
        if (weight != other.weight) return false
        if (weaponDisplayAccuracy != other.weaponDisplayAccuracy) return false
        if (weaponDisplayRateOfFire != other.weaponDisplayRateOfFire) return false
        if (weaponDisplayRange != other.weaponDisplayRange) return false
        if (numLegendaryStars != other.numLegendaryStars) return false
        if (itemLevel != other.itemLevel) return false
        if (rarity != other.rarity) return false
        if (isTradable != other.isTradable) return false
        if (isSpoiled != other.isSpoiled) return false
        if (isSetItem != other.isSetItem) return false
        if (isQuestItem != other.isQuestItem) return false
        if (isLegendary != other.isLegendary) return false
        if (itemCardEntries != other.itemCardEntries) return false
        if (vendingData != other.vendingData) return false
        if (modCardEntries != other.modCardEntries) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ownerInfo?.hashCode() ?: 0
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (serverHandleId?.hashCode() ?: 0)
        result = 31 * result + (count ?: 0)
        result = 31 * result + itemValue
        result = 31 * result + (filterFlag ?: 0)
        result = 31 * result + (currentHealth ?: 0)
        result = 31 * result + (damage ?: 0)
        result = 31 * result + (durability ?: 0)
        result = 31 * result + (maximumHealth ?: 0)
        result = 31 * result + (weight?.hashCode() ?: 0)
        result = 31 * result + (weaponDisplayAccuracy?.hashCode() ?: 0)
        result = 31 * result + (weaponDisplayRateOfFire?.hashCode() ?: 0)
        result = 31 * result + (weaponDisplayRange?.hashCode() ?: 0)
        result = 31 * result + numLegendaryStars
        result = 31 * result + (itemLevel ?: 0)
        result = 31 * result + (rarity ?: 0)
        result = 31 * result + isTradable.hashCode()
        result = 31 * result + isSpoiled.hashCode()
        result = 31 * result + isSetItem.hashCode()
        result = 31 * result + isQuestItem.hashCode()
        result = 31 * result + isLegendary.hashCode()
        result = 31 * result + (itemCardEntries?.hashCode() ?: 0)
        result = 31 * result + (vendingData?.hashCode() ?: 0)
        result = 31 * result + (modCardEntries?.hashCode() ?: 0)
        return result
    }
}