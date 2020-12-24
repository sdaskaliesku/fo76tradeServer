package com.manson.fo76.service

import com.manson.domain.LegendaryMod
import com.manson.domain.config.LegendaryModDescriptor
import com.manson.domain.fed76.FedModDataRequest
import com.manson.domain.fo76.items.enums.DamageType
import com.manson.domain.fo76.items.enums.FilterFlag
import com.manson.domain.fo76.items.enums.ItemCardText
import com.manson.domain.fo76.items.item_card.ItemCardEntry
import com.manson.domain.itemextractor.CharacterInventory
import com.manson.domain.itemextractor.ItemDescriptor
import com.manson.domain.itemextractor.ModData
import com.manson.domain.itemextractor.OwnerInfo
import com.manson.fo76.domain.ItemsUploadFilters
import com.manson.fo76.domain.ModDataRequest
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.ItemDetails
import com.manson.fo76.domain.dto.StatsDTO
import com.manson.fo76.domain.dto.TradeOptions
import com.manson.fo76.helper.Utils.areSameItems
import com.manson.fo76.helper.Utils.silentParse
import java.util.Comparator
import java.util.Objects
import java.util.UUID
import java.util.stream.Collectors
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.MapUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemConverterService @Autowired constructor(private val gameConfigService: GameConfigService, private val fed76Service: Fed76Service) {

    @Suppress("UnstableApiUsage")
    companion object {
        private val LOGGER = LoggerFactory.getLogger(ItemConverterService::class.java)
        private val IGNORED_CARDS: MutableSet<ItemCardText> = mutableSetOf(ItemCardText.LEG_MODS, ItemCardText.DESC)
        private val TYPES_FOR_NAME_CONVERT: MutableSet<FilterFlag> = mutableSetOf(FilterFlag.ARMOR, FilterFlag.WEAPON, FilterFlag.WEAPON_MELEE, FilterFlag.WEAPON_RANGED)
        private val ARMOR_TYPES: MutableSet<FilterFlag> = mutableSetOf(FilterFlag.ARMOR, FilterFlag.ARMOR_OUTFIT, FilterFlag.POWER_ARMOR)
        val SUPPORTED_PRICE_CHECK_ITEMS: MutableSet<FilterFlag> = mutableSetOf(FilterFlag.ARMOR, FilterFlag.WEAPON_MELEE, FilterFlag.WEAPON_RANGED)

        private fun matchesFilter(filter: ItemsUploadFilters, itemDescriptor: ItemDescriptor): Boolean {
            if (filter.tradableOnly) {
                if (!itemDescriptor.isTradable) {
//                    LOGGER.debug("Skipping item(filter - tradableOnly) $itemDescriptor")
                    return false
                }
            }
            if (filter.legendaryOnly) {
                if (!itemDescriptor.isLegendary) {
//                    LOGGER.debug("Skipping item(filter - legendaryOnly) $itemDescriptor")
                    return false
                }
            }
            val filterFlag = itemDescriptor.filterFlagEnum
            if (filterFlag == FilterFlag.UNKNOWN) {
                LOGGER.warn("Empty filter flag for {}", itemDescriptor)
                return false
            }
            if (CollectionUtils.isEmpty(filter.filterFlags)) {
                return true
            }
            for (flag in filterFlag.getFlags()) {
                if (filter.filterFlags.contains(flag)) {
                    return true
                }
            }
//            LOGGER.debug("Skipping item(filter - filterFlags) $itemDescriptor")
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

    private fun processModDataItems(modData: ModData?, itemsUploadFilters: ItemsUploadFilters): List<ItemDescriptor> {
        if (modData == null || MapUtils.isEmpty(modData.characterInventories)) {
            return listOf()
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
        return allItems
    }

    fun prepareModData(fedModDataRequest: FedModDataRequest, autoPriceCheck: Boolean): List<ItemDTO?> {
        val modData = ModData()
        val map: Map<String, List<ItemDescriptor>> = fedModDataRequest.characterInventories
        val characterInventory = CharacterInventory()
        val list: MutableList<ItemDescriptor> = ArrayList()
        map.forEach { (_, v) -> list.addAll(v) }
        characterInventory.playerInventory = list
        modData.characterInventories = HashMap()
        modData.characterInventories["test"] = characterInventory
        val itemDescriptors = processModDataItems(modData, ItemsUploadFilters())
        return convertItems(itemDescriptors, autoPriceCheck)
    }

    fun prepareModData(modDataRequest: ModDataRequest, autoPriceCheck: Boolean): List<ItemDTO?> {
        val itemDescriptors = processModDataItems(modDataRequest.modData, modDataRequest.filters)
        return convertItems(itemDescriptors, autoPriceCheck)
    }

    private fun dedupeItems(itemDTOS: MutableList<ItemDTO>, autoPriceCheck: Boolean): List<ItemDTO?> {
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
                // TODO: temp
                itemDTO.id = UUID.randomUUID().toString()
                itemDTO.itemDetails = createItemDetails(itemDTO, autoPriceCheck)
                deduped.add(itemDTO)
            }
        }
        return deduped
    }

    private fun convertItems(items: List<ItemDescriptor>, autoPriceCheck: Boolean): List<ItemDTO?> {
        val list: MutableList<ItemDTO> = items.stream().map { item: ItemDescriptor -> convertItem(item) }
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        return dedupeItems(list, autoPriceCheck)
    }

    private fun findFilterFlag(item: ItemDescriptor?): FilterFlag {
        var filterFlag = item!!.filterFlagEnum
        try {
            if (filterFlag === FilterFlag.WEAPON) {
                for (itemCardEntry in item.itemCardEntries) {
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

    private fun createTradeOptions(item: ItemDTO, itemDescriptor: ItemDescriptor): TradeOptions {
        val tradeOptions = TradeOptions()
        val itemValue = item.itemValue
        tradeOptions.gamePrice = itemValue.toDouble()
        if (Objects.nonNull(itemDescriptor.vendingData)) {
            var price = itemDescriptor.vendingData.price
            if (price == 0) {
                price = itemValue
            }
            tradeOptions.vendorPrice = price!!.toDouble()
        } else {
            tradeOptions.vendorPrice = tradeOptions.gamePrice
        }
        return tradeOptions
    }

    private fun createOwnerInfo(item: ItemDTO): OwnerInfo {
        var ownerInfo = OwnerInfo()
        if (Objects.nonNull(item.ownerInfo)) {
            ownerInfo = item.ownerInfo!!
        }
        // TODO: get user from request header's token
        ownerInfo.id = "NONE"
        ownerInfo.name = "NONE"
        return ownerInfo
    }

    private fun createItemDetails(item: ItemDTO, autoPriceCheck: Boolean): ItemDetails {
        val itemDetails = item.itemDetails
        itemDetails.armorGrade = gameConfigService.findArmorType(item)
        if (shouldConvertItemName(item)) {
            itemDetails.name = item.text?.let { gameConfigService.getPossibleItemName(it, isArmor(item)) }.toString()
            itemDetails.fedName = gameConfigService.findFedItemName(item)
            if (!autoPriceCheck) {
                return itemDetails
            }
            if (item.isLegendary && item.isTradable && SUPPORTED_PRICE_CHECK_ITEMS.contains(item.filterFlag)) {
                val request = fed76Service.createPriceCheckRequest(item)
                if (request.isValid()) {
                    itemDetails.priceCheckResponse = fed76Service.priceCheck(request)
                } else {
                    LOGGER.error("Request is invalid, ignoring: $request\r\n$item")
                }
            }
        }
        return itemDetails
    }

    private fun convertItem(item: ItemDescriptor): ItemDTO? {
        val objectMap: MutableMap<String, Any?>? = JsonParser.objectToMap(item)
        if (MapUtils.isEmpty(objectMap)) {
            return null
        }
        objectMap?.put("filterFlag", findFilterFlag(item))
        val itemDTO = JsonParser.mapToItemDTO(objectMap)
        if (Objects.isNull(itemDTO)) {
            return null
        }
        val itemDto: ItemDTO = itemDTO!!
        itemDto.ownerInfo = createOwnerInfo(itemDto)
        itemDto.tradeOptions = createTradeOptions(itemDto, item)
        itemDto.stats = processItemCardEntries(item, itemDto)
        itemDto.text = itemDto.text?.let { gameConfigService.cleanItemName(it) }
        return itemDTO
    }

    private fun isArmor(item: ItemDTO): Boolean {
        return ARMOR_TYPES.contains(item.filterFlag)
    }

    private fun processLegendaryMods(itemCardEntry: ItemCardEntry, itemCardText: ItemCardText, filterFlag: FilterFlag): MutableList<LegendaryMod> {
        val mods: MutableList<LegendaryMod> = ArrayList()
        if (itemCardText === ItemCardText.DESC) {
            val strings = itemCardEntry.value!!.split("\n").toTypedArray()
            for (mod in strings) {
                val newMod = mod.trim()
                if (StringUtils.isBlank(newMod)) {
                    continue
                }
                val legendaryMod = LegendaryMod(newMod)
                val descriptor: LegendaryModDescriptor? = gameConfigService.findLegendaryModDescriptor(newMod, filterFlag)
                if (descriptor != null) {
                    legendaryMod.abbreviation = descriptor.abbreviation.toString()
                    legendaryMod.star = descriptor.star
                    legendaryMod.id = descriptor.id
                    legendaryMod.gameId = descriptor.gameId.toString()
                    legendaryMod.text = descriptor.texts["en"].toString()
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
        for (itemCardEntry in item.itemCardEntries) {
            val itemCardText = gameConfigService.findItemCardText(itemCardEntry)
            val statsDTO = convertItemStats(itemCardEntry, itemCardText)
            if (statsDTO != null) {
                stats.add(statsDTO)
            } else if (itemCardText === ItemCardText.DESC) {
                if (itemDTO.isLegendary) {
                    val legendaryMods: List<LegendaryMod> = processLegendaryMods(itemCardEntry, itemCardText, itemDTO.filterFlag)
                    if (CollectionUtils.isEmpty(legendaryMods)) {
                        continue
                    }
                    itemDTO.legendaryMods = legendaryMods
                    var abbreviation = legendaryMods.stream()
                            .map(LegendaryMod::abbreviation)
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining("/"))
                    if (StringUtils.isBlank(abbreviation)) {
                        abbreviation = StringUtils.EMPTY
                    }
                    itemDTO.itemDetails.abbreviation = abbreviation.toUpperCase()
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