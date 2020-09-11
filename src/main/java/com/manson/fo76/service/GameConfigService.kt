package com.manson.fo76.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.Resources
import com.manson.fo76.domain.ArmorConfig
import com.manson.fo76.domain.LegendaryModDescriptor
import com.manson.fo76.domain.XTranslatorConfig
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.StatsDTO
import com.manson.fo76.domain.items.enums.ArmorGrade
import com.manson.fo76.domain.items.enums.DamageType
import com.manson.fo76.domain.items.enums.ItemCardText
import com.manson.fo76.domain.items.item_card.ItemCardEntry
import java.util.function.Predicate
import java.util.stream.Collectors
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Suppress("UnstableApiUsage")
class GameConfigService @Autowired constructor(objectMapper: ObjectMapper) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(GameConfigService::class.java)
        private const val LEG_MODS_CONFIG_FILE = "legendaryMods.config.json"
        private const val AMMO_TYPES_CONFIG_FILE = "ammo.types.json"
        private const val ARMOR_CONFIG_FILE = "armor.config.json"
        private val LEG_MOD_TYPE_REF: TypeReference<List<LegendaryModDescriptor>> = object : TypeReference<List<LegendaryModDescriptor>>() {}
        private val XTRANSLATOR_TYPE_REF: TypeReference<List<XTranslatorConfig>> = object : TypeReference<List<XTranslatorConfig>>() {}
        private val ARMOR_CONFIG_TYPE_REF: TypeReference<List<ArmorConfig>> = object : TypeReference<List<ArmorConfig>>() {}

        private fun <T> loadConfig(objectMapper: ObjectMapper, file: String, typeReference: TypeReference<List<T>>, predicate: Predicate<T>): List<T> {
            var configs: List<T> = ArrayList()
            try {
                val resource = Resources.getResource(file)
                configs = objectMapper.readValue(resource, typeReference).stream()
                        .filter(predicate)
                        .collect(Collectors.toList())
            } catch (e: Exception) {
                LOGGER.error("Error while loading game config", e)
            }
            return configs
        }
    }

    var legModsConfig: List<LegendaryModDescriptor>
    var ammoTypes: List<XTranslatorConfig>
    var armorConfigs: List<ArmorConfig>

    init {
        this.legModsConfig = loadConfig(objectMapper, LEG_MODS_CONFIG_FILE, LEG_MOD_TYPE_REF, XTranslatorConfig::enabled)
        this.ammoTypes = loadConfig(objectMapper, AMMO_TYPES_CONFIG_FILE, XTRANSLATOR_TYPE_REF, XTranslatorConfig::enabled)
        this.armorConfigs = loadConfig(objectMapper, ARMOR_CONFIG_FILE, ARMOR_CONFIG_TYPE_REF, { true })
    }

    fun findItemCardText(cardEntry: ItemCardEntry): ItemCardText {
        return findItemCardText(cardEntry.text)
    }

    fun findItemCardText(input: String?): ItemCardText {
        if (StringUtils.isBlank(input)) {
            return ItemCardText.UNKNOWN
        }
        ItemCardText.values().forEach { item ->
            item.getValues().forEach { value ->
                if (StringUtils.equalsIgnoreCase(value, input)) {
                    return item
                }
            }
        }
        return if (ammoTypes.any { it.texts.values.contains(input) }) ItemCardText.AMMO else ItemCardText.UNKNOWN
    }

    fun findLegendaryModDescriptor(input: String?): LegendaryModDescriptor? {
        if (StringUtils.isBlank(input)) {
            return null
        }
        val value: String = input.toString()
        return legModsConfig.firstOrNull { it.isTheSameMod(value) }
    }

    private fun findDamageTypeValue(stats: List<StatsDTO>, dmgType: DamageType): Int {
        for (stat in stats) {
            if (stat.damageType === dmgType) {
                if (StringUtils.isBlank(stat.value)) {
                    return 0
                }
                return Integer.valueOf(stat.value)
            }
        }
        return 0
    }

    fun findArmorType(dr: Int, rr: Int, er: Int): ArmorGrade {
        for (config in armorConfigs) {
            if (config.dr == dr && config.er == er && config.rr == rr) {
                for (armorType in ArmorGrade.values()) {
                    if (StringUtils.containsIgnoreCase(config.armorGrade.value, armorType.value)) {
                        return armorType
                    }
                }
            }
        }
        return ArmorGrade.Unknown
    }

    fun findArmorType(itemDTO: ItemDTO): ArmorGrade {
        val dr: Int = findDamageTypeValue(itemDTO.stats, DamageType.BALLISTIC)
        val er: Int = findDamageTypeValue(itemDTO.stats, DamageType.ENERGY)
        var rr: Int = findDamageTypeValue(itemDTO.stats, DamageType.RADIATION)
        if (StringUtils.containsIgnoreCase(itemDTO.abbreviation, "25R")) {
            rr -= 25
        }
        return findArmorType(dr, rr, er)
    }
}