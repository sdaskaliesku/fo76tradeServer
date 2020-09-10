package com.manson.fo76.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.manson.fo76.domain.items.enums.ArmorType
import com.manson.fo76.domain.items.enums.FilterFlag
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

@JsonIgnoreProperties(ignoreUnknown = true)
class ItemDTO {
    @Id
    var id: String? = null

    @Indexed
    var ownerInfo: OwnerInfo? = null

    @Indexed
    var tradeOptions: TradeOptions? = null

    @Indexed
    var text: String? = null

    @Indexed
    var description: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var serverHandleId: Long = 0
    var count: Int = 0
    var itemValue: Int = 0

    @Indexed
    var filterFlag: FilterFlag = FilterFlag.UNKNOWN
    var currentHealth: Int = 0
    var damage: Int = 0
    var durability: Int = 0
    var maximumHealth: Int = 0
    var weight: Double = 0.0
    var weaponDisplayAccuracy: Double = 0.0
    var weaponDisplayRateOfFire: Double = 0.0
    var weaponDisplayRange: Double = 0.0

    @Indexed
    var numLegendaryStars: Int = 0

    @Indexed
    var itemLevel: Int = 0
    var rarity: Int = 0
    var isTradable: Boolean = false
    var isSpoiled: Boolean = false
    var isSetItem: Boolean = false
    var isQuestItem: Boolean = false

    @Indexed
    var isLegendary: Boolean = false

    @Indexed
    var stats: List<StatsDTO> = listOf()

    @Indexed
    var legendaryMods: List<LegendaryMod> = listOf()
    var abbreviation: String = ""

    var armorType: ArmorType = ArmorType.Unknown
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemDTO) return false

        if (id != other.id) return false
        if (ownerInfo != other.ownerInfo) return false
        if (tradeOptions != other.tradeOptions) return false
        if (text != other.text) return false
        if (description != other.description) return false
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
        if (stats != other.stats) return false
        if (legendaryMods != other.legendaryMods) return false
        if (abbreviation != other.abbreviation) return false
        if (armorType != other.armorType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (ownerInfo?.hashCode() ?: 0)
        result = 31 * result + (tradeOptions?.hashCode() ?: 0)
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + serverHandleId.hashCode()
        result = 31 * result + count
        result = 31 * result + itemValue
        result = 31 * result + filterFlag.hashCode()
        result = 31 * result + currentHealth
        result = 31 * result + damage
        result = 31 * result + durability
        result = 31 * result + maximumHealth
        result = 31 * result + weight.hashCode()
        result = 31 * result + weaponDisplayAccuracy.hashCode()
        result = 31 * result + weaponDisplayRateOfFire.hashCode()
        result = 31 * result + weaponDisplayRange.hashCode()
        result = 31 * result + numLegendaryStars
        result = 31 * result + itemLevel
        result = 31 * result + rarity
        result = 31 * result + isTradable.hashCode()
        result = 31 * result + isSpoiled.hashCode()
        result = 31 * result + isSetItem.hashCode()
        result = 31 * result + isQuestItem.hashCode()
        result = 31 * result + isLegendary.hashCode()
        result = 31 * result + stats.hashCode()
        result = 31 * result + legendaryMods.hashCode()
        result = 31 * result + abbreviation.hashCode()
        result = 31 * result + armorType.hashCode()
        return result
    }


}