package com.manson.fo76.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.Resources
import com.manson.fo76.domain.LegendaryModDescriptor
import com.manson.fo76.domain.XTranslatorConfig
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
        private val LEG_MOD_TYPE_REF: TypeReference<List<LegendaryModDescriptor>> = object : TypeReference<List<LegendaryModDescriptor>>() {}
        private val XTRANSLATOR_TYPE_REF: TypeReference<List<XTranslatorConfig>> = object : TypeReference<List<XTranslatorConfig>>() {}

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

    init {
        this.legModsConfig = loadConfig(objectMapper, LEG_MODS_CONFIG_FILE, LEG_MOD_TYPE_REF, XTranslatorConfig::enabled)
        this.ammoTypes = loadConfig(objectMapper, AMMO_TYPES_CONFIG_FILE, XTRANSLATOR_TYPE_REF, XTranslatorConfig::enabled)
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
}