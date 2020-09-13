package com.manson.fo76.domain.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.manson.fo76.domain.AbstractObject
import com.manson.fo76.domain.dto.OwnerInfo
import com.manson.fo76.domain.items.enums.FilterFlag
import com.manson.fo76.domain.items.enums.FilterFlag.Companion.fromInt
import com.manson.fo76.domain.items.item_card.ItemCardEntry
import com.manson.fo76.domain.items.mod_card.ModCardEntry
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
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
    var numLegendaryStars: Int? = null
    var itemLevel: Int? = null
    var rarity: Int? = null
    var tradable: Boolean? = null
    var spoiled: Boolean? = null
    var setItem: Boolean? = null
    var questItem: Boolean? = null
    var legendary: Boolean? = null

    @JsonProperty("ItemCardEntries")
    var itemCardEntries: List<ItemCardEntry>? = null
    var vendingData: VendingData? = null

    @JsonProperty("ModCardEntries")
    var modCardEntries: List<ModCardEntry>? = null

    @get:JsonIgnore
    val filterFlagEnum: FilterFlag
        get() = fromInt(filterFlag)

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as ItemDescriptor
        return EqualsBuilder()
                .append(text, that.text)
                .append(serverHandleId, that.serverHandleId)
                .append(count, that.count)
                .append(itemValue, that.itemValue)
                .append(filterFlag, that.filterFlag)
                .append(currentHealth, that.currentHealth)
                .append(damage, that.damage)
                .append(durability, that.durability)
                .append(maximumHealth, that.maximumHealth)
                .append(weight, that.weight)
                .append(weaponDisplayAccuracy, that.weaponDisplayAccuracy)
                .append(weaponDisplayRateOfFire, that.weaponDisplayRateOfFire)
                .append(weaponDisplayRange, that.weaponDisplayRange)
                .append(numLegendaryStars, that.numLegendaryStars)
                .append(itemLevel, that.itemLevel)
                .append(rarity, that.rarity)
                .append(tradable, that.tradable)
                .append(spoiled, that.spoiled)
                .append(setItem, that.setItem)
                .append(questItem, that.questItem)
                .append(legendary, that.legendary)
                .append(itemCardEntries, that.itemCardEntries)
                .append(vendingData, that.vendingData)
                .append(modCardEntries, that.modCardEntries)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(text)
                .append(serverHandleId)
                .append(count)
                .append(itemValue)
                .append(filterFlag)
                .append(currentHealth)
                .append(damage)
                .append(durability)
                .append(maximumHealth)
                .append(weight)
                .append(weaponDisplayAccuracy)
                .append(weaponDisplayRateOfFire)
                .append(weaponDisplayRange)
                .append(numLegendaryStars)
                .append(itemLevel)
                .append(rarity)
                .append(tradable)
                .append(spoiled)
                .append(setItem)
                .append(questItem)
                .append(legendary)
                .append(itemCardEntries)
                .append(vendingData)
                .append(modCardEntries)
                .toHashCode()
    }

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
}