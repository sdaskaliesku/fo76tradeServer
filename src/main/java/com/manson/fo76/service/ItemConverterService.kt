package com.manson.fo76.service

import com.manson.fo76.domain.ItemsUploadFilters
import com.manson.fo76.domain.LegendaryModDescriptor
import com.manson.fo76.domain.ModData
import com.manson.fo76.domain.ModDataRequest
import com.manson.fo76.domain.ModUser
import com.manson.fo76.domain.User
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.LegendaryMod
import com.manson.fo76.domain.dto.OwnerInfo
import com.manson.fo76.domain.dto.StatsDTO
import com.manson.fo76.domain.dto.TradeOptions
import com.manson.fo76.domain.items.ItemDescriptor
import com.manson.fo76.domain.items.enums.ArmorGrade
import com.manson.fo76.domain.items.enums.DamageType
import com.manson.fo76.domain.items.enums.FilterFlag
import com.manson.fo76.domain.items.enums.ItemCardText
import com.manson.fo76.domain.items.item_card.ItemCardEntry
import java.util.Comparator
import java.util.Objects
import java.util.stream.Collectors
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.MapUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.apache.commons.lang3.tuple.ImmutablePair
import org.apache.commons.lang3.tuple.Pair
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemConverterService @Autowired constructor(private val gameConfigService: GameConfigService) {

    private val IGNORED_CARDS: MutableSet<ItemCardText> = mutableSetOf(ItemCardText.LEG_MODS, ItemCardText.DESC)
    private val TYPES_FOR_NAME_CONVERT: MutableSet<FilterFlag> = mutableSetOf(FilterFlag.ARMOR, FilterFlag.WEAPON, FilterFlag.WEAPON_MELEE, FilterFlag.WEAPON_RANGED)

    private fun processModDataItems(
            modData: ModData?,
            itemsUploadFilters: ItemsUploadFilters,
    ): Pair<ModUser?, List<ItemDescriptor>> {
        if (modData == null || MapUtils.isEmpty(modData.characterInventories)) {
            return ImmutablePair(ModUser(), listOf())
        }
        if (modData.user == null) {
            // TODO: delete this, once final UI will be ready
            val user = ModUser()
            user.user = "temp"
            user.password = "temp"
            modData.user = user
        }
        val values = modData.characterInventories.values
        val allItems: MutableList<ItemDescriptor> = ArrayList()
        for (inventory in values) {
            val allCharacterItems: MutableList<ItemDescriptor> = ArrayList()
            allCharacterItems.addAll(inventory.playerInventory)
            allCharacterItems.addAll(inventory.stashInventory)
            processItems(allCharacterItems, allItems, itemsUploadFilters, inventory.accountInfoData.name,
                    inventory.characterInfoData.name)
        }
        return ImmutablePair(modData.user, allItems)
    }

    fun prepareModData(modDataRequest: ModDataRequest): List<ItemDTO?>? {
        val pair = processModDataItems(modDataRequest.modData, modDataRequest.filters)
        val itemDescriptors = pair.value
        // todo: convert ModUser to User via userService
        val modUser = pair.key
        val user = User()
        user.password = modUser!!.password
        user.name = modUser.user
        user.id = modUser.id
        return convertItems(itemDescriptors, user)
    }

    @Suppress("UnstableApiUsage")
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ItemConverterService::class.java)

        private fun matchesFilter(filter: ItemsUploadFilters, itemDescriptor: ItemDescriptor?): Boolean {
            if (filter.tradableOnly) {
                if (itemDescriptor!!.tradable == null || !itemDescriptor.tradable!!) {
                    return false
                }
            }
            val filterFlag = itemDescriptor!!.filterFlagEnum
            if (filterFlag == FilterFlag.UNKNOWN) {
                LOGGER.warn("Empty filter flag for {}", itemDescriptor)
                return false
            } else {
                if (filter.legendaryOnly) {
                    val stars = itemDescriptor.numLegendaryStars
                    if (stars == null || stars <= 0) {
                        return false
                    }
                }
                if (CollectionUtils.isEmpty(filter.filterFlags)) {
                    return true
                } else {
                    for (flag in filterFlag.getFlags()) {
                        if (filter.filterFlags.contains(flag)) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        private fun processItems(
                items: List<ItemDescriptor>, outputList: MutableList<ItemDescriptor>,
                itemsUploadFilters: ItemsUploadFilters, account: String?, character: String?,
        ) {
            if (CollectionUtils.isEmpty(items)) {
                return
            }
            for (itemDescriptor in items) {
                if (matchesFilter(itemsUploadFilters, itemDescriptor)) {
                    val ownerInfo = OwnerInfo()
                    ownerInfo.accountOwner = account
                    ownerInfo.characterOwner = character
                    itemDescriptor.ownerInfo = ownerInfo
                    outputList.add(itemDescriptor)
                }
            }
        }
    }

    private fun areSameItems(first: ItemDTO, second: ItemDTO?): Boolean {
        val sameName = StringUtils.equalsIgnoreCase(first.text, second!!.text)
        val sameLevel = NumberUtils.compare(first.itemLevel, second.itemLevel) == 0
        val sameLegMods = StringUtils.equalsIgnoreCase(first.abbreviation, second.abbreviation)
        return sameName && sameLevel && sameLegMods
    }

    private fun dedupeItems(itemDTOS: MutableList<ItemDTO>): List<ItemDTO?> {
        val deduped: MutableList<ItemDTO> = java.util.ArrayList()
        for (itemDTO in itemDTOS) {
            if (CollectionUtils.isEmpty(deduped)) {
                deduped.add(itemDTO)
                continue
            }
            var isDuplicate = false
            for (item in deduped) {
                if (areSameItems(itemDTO, item)) {
                    item.count = item.count + 1
                    isDuplicate = true
                    break
                }
            }
            if (!isDuplicate) {
                deduped.add(itemDTO)
            }
        }
        return deduped
    }

    private fun convertItems(items: List<ItemDescriptor>, user: User): List<ItemDTO?> {
        val list: MutableList<ItemDTO> = items.stream().map { item: ItemDescriptor -> convertItem(item, user) }
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        return dedupeItems(list)
    }

    private fun silentParse(value: String?): Number {
        try {
            return java.lang.Double.valueOf(value)
        } catch (ignored: Exception) {
        }
        return -1
    }

    private fun findFilterFlag(item: ItemDescriptor?): FilterFlag {
        var filterFlag = item!!.filterFlagEnum
        try {
            if (filterFlag === FilterFlag.WEAPON) {
                for (itemCardEntry in item.itemCardEntries!!) {
                    if (itemCardEntry.damageTypeEnum === DamageType.AMMO) {
                        filterFlag = FilterFlag.WEAPON_RANGED
                        break
                    }
                    val cardText = gameConfigService.findItemCardText(itemCardEntry)
                    if (cardText === ItemCardText.ROF && silentParse(itemCardEntry.value).toDouble() > 0) {
                        filterFlag = FilterFlag.WEAPON_RANGED
                        break
                    }
                    if (cardText === ItemCardText.MELEE_SPEED) {
                        filterFlag = FilterFlag.WEAPON_MELEE
                        break
                    }
                    if (cardText === ItemCardText.RNG && silentParse(itemCardEntry.value).toDouble() > 0) {
                        filterFlag = FilterFlag.WEAPON_THROWN
                        break
                    }
                }
            }
            if (filterFlag === FilterFlag.ARMOR && item.currentHealth == -1) {
                filterFlag = FilterFlag.ARMOR_OUTFIT
            }
        } catch (e: Exception) {
            LOGGER.error("Error while looking for specific filter flag {}", item, e)
        }
        return filterFlag
    }

    private fun shouldConvertItemName(item: ItemDTO): Boolean {
        return TYPES_FOR_NAME_CONVERT.contains(item.filterFlag)
    }

    private fun convertItem(item: ItemDescriptor, user: User): ItemDTO? {
        val objectMap: MutableMap<String, Any?>? = JsonParser.objectToMap(item)
        if (MapUtils.isEmpty(objectMap)) {
            return null
        }
        objectMap?.put("filterFlag", findFilterFlag(item))
        val itemDTO = JsonParser.mapToItemDTO(objectMap)
        if (Objects.isNull(itemDTO)) {
            return null
        }
        var ownerInfo = OwnerInfo()
        if (Objects.nonNull(itemDTO!!.ownerInfo)) {
            ownerInfo = itemDTO.ownerInfo!!
        }
        ownerInfo.id = user.id
        ownerInfo.name = user.name
        val tradeOptions = TradeOptions()
        val itemValue = item.itemValue
        tradeOptions.gamePrice = itemValue!!.toDouble()
        if (Objects.nonNull(item.vendingData)) {
            var price = item.vendingData!!.price
            if (price == 0) {
                price = itemValue
            }
            tradeOptions.vendorPrice = price!!.toDouble()
        } else {
            tradeOptions.vendorPrice = tradeOptions.gamePrice
        }
        itemDTO.tradeOptions = tradeOptions
        itemDTO.ownerInfo = ownerInfo
        itemDTO.stats = processItemCardEntries(item, itemDTO)
        itemDTO.armorGrade = gameConfigService.findArmorType(itemDTO)
        itemDTO.text = itemDTO.text?.let { gameConfigService.cleanItemName(it) }
        if (shouldConvertItemName(itemDTO)) {
            val isArmor: Boolean = listOf(FilterFlag.ARMOR, FilterFlag.ARMOR_OUTFIT, FilterFlag.POWER_ARMOR).contains(itemDTO.filterFlag)
            itemDTO.newName = itemDTO.text?.let { gameConfigService.getPossibleItemName(it, isArmor) }.toString()
        }
        return itemDTO
    }

    private fun processLegendaryMods(itemCardEntry: ItemCardEntry, itemCardText: ItemCardText): MutableList<LegendaryMod> {
        val mods: MutableList<LegendaryMod> = ArrayList()
        if (itemCardText === ItemCardText.DESC) {
            val strings = itemCardEntry.value!!.split("\n").toTypedArray()
            for (mod in strings) {
                val newMod = mod.trim()
                if (StringUtils.isBlank(newMod)) {
                    continue
                }
                val legendaryMod = LegendaryMod(newMod)
                val descriptor: LegendaryModDescriptor? = gameConfigService.findLegendaryModDescriptor(newMod)
                if (descriptor != null) {
                    legendaryMod.abbreviation = descriptor.abbreviation
                    legendaryMod.star = descriptor.star
                    legendaryMod.id = descriptor.id
                }
                mods.add(legendaryMod)
            }
        }
        if (CollectionUtils.isNotEmpty(mods)) {
            mods.sortWith(Comparator.comparingInt(LegendaryMod::star))
        }
        return mods
    }

    private fun processItemCardEntries(item: ItemDescriptor, itemDTO: ItemDTO): List<StatsDTO> {
        val stats: MutableList<StatsDTO> = ArrayList()
        if (CollectionUtils.isEmpty(item.itemCardEntries)) {
            return stats
        }
        for (itemCardEntry in item.itemCardEntries!!) {
            val itemCardText = gameConfigService.findItemCardText(itemCardEntry)
            val statsDTO = convertItemStats(itemCardEntry, itemCardText)
            if (statsDTO != null) {
                stats.add(statsDTO)
            } else if (itemCardText === ItemCardText.DESC) {
                if (itemDTO.filterFlag.isHasStarMods || itemDTO.isLegendary) {
                    val legendaryMods: List<LegendaryMod> = processLegendaryMods(itemCardEntry, itemCardText)
                    if (CollectionUtils.isEmpty(legendaryMods)) {
                        continue
                    }
                    itemDTO.legendaryMods = legendaryMods
                    var abbreviation = legendaryMods.stream()
                            .map<String>(LegendaryMod::abbreviation)
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining("/"))
                    if (StringUtils.isBlank(abbreviation)) {
                        abbreviation = StringUtils.EMPTY
                    }
                    itemDTO.abbreviation = abbreviation
                } else {
                    itemDTO.description = itemCardEntry.value
                }
            }
        }
        return stats
    }

    private fun convertItemStats(itemCardEntry: ItemCardEntry, itemCardText: ItemCardText): StatsDTO? {
        if (IGNORED_CARDS.contains(itemCardText)) {
            return null
        }
        val objectMap: MutableMap<String, Any?>? = JsonParser.objectToMap(itemCardEntry)
        if (MapUtils.isEmpty(objectMap)) {
            return null
        }
        objectMap?.put("text", itemCardText)
        objectMap?.put("damageType", itemCardEntry.damageTypeEnum)
        return JsonParser.mapToStatsDTO(objectMap)
    }
}