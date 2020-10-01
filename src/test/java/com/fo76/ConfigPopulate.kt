package com.fo76

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.domain.config.Fo76String
import com.manson.domain.config.LegendaryModDescriptor
import com.manson.domain.config.XTranslatorConfig
import com.manson.domain.fo76.items.enums.FilterFlag
import com.manson.domain.itemextractor.ModData
import com.manson.domain.nuka.NukaRequest
import com.manson.domain.nuka.NukaRow
import com.manson.fo76.domain.ItemsUploadFilters
import com.manson.fo76.domain.ModDataRequest
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.repository.PriceCheckRepository
import com.manson.fo76.service.Fed76Service
import com.manson.fo76.service.GameConfigService
import com.manson.fo76.service.ItemConverterService
import com.manson.fo76.service.ItemService
import com.manson.fo76.service.NukaCryptService
import com.manson.fo76.service.XTranslatorParser
import java.io.File
import java.util.HashMap
import java.util.stream.Collectors
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Test


class ConfigPopulate {

    companion object {
        private val OM = ObjectMapper()
        private val xTranslatorParser = XTranslatorParser(OM)
        private val LEG_MOD_TYPE_REF: TypeReference<List<LegendaryModDescriptor>> = object : TypeReference<List<LegendaryModDescriptor>>() {}
        private val XTRANSLATOR_TYPE_REF: TypeReference<List<XTranslatorConfig>> = object : TypeReference<List<XTranslatorConfig>>() {}
        private val ITEM_DTO_TYPE_REF: TypeReference<List<ItemDTO>> = object : TypeReference<List<ItemDTO>>() {}
        private val fed76Service: Fed76Service = Fed76Service(null, OM)
        private val gameConfigService: GameConfigService = GameConfigService(OM)
        private val itemConverterService: ItemConverterService = ItemConverterService(gameConfigService, fed76Service)
        private val itemService: ItemService = ItemService(null, null, itemConverterService)
        private val locale = "pl"
        private fun parseXml(file: File): Map<String, Fo76String> {
            val strings: List<Fo76String> = xTranslatorParser.parse(file)
            return genericListToFo76List(strings)
        }

        fun genericListToFo76List(fo76Strings: List<Fo76String>): Map<String, Fo76String> {
            var map: Map<String, Fo76String> = HashMap()
            try {
                map = fo76Strings.stream().collect(Collectors.toMap(Fo76String::edid, { fo76String: Fo76String -> fo76String }) { _: Fo76String, b: Fo76String -> b })
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return map
        }
    }

    @Test
    fun legMods() {
        // xTranlator query = 'mod_Legendary_'
        val inputFileName = "legendaryMods.config.json"
        val configFile = File("src/main/resources/$inputFileName")
        val legMods: List<LegendaryModDescriptor> = OM.readValue(configFile, LEG_MOD_TYPE_REF)
        val keysFile = File("test_resources/leg_mods/$locale/keys.xml")
        val valuesFile = File("test_resources/leg_mods/$locale/values.xml")
        val values: Map<String, Fo76String> = parseXml(valuesFile)
        val keys: Map<String, Fo76String> = parseXml(keysFile)

        legMods.forEach { mod ->
            if (keys.containsKey(mod.id)) {
                mod.texts[locale] = keys[mod.id]?.source.toString()
            }
            if (values.containsKey(mod.id)) {
                mod.translations[locale] = values[mod.id]?.source.toString()
            }
        }

        OM.writeValue(File(inputFileName), legMods)
    }

    @Test
    fun nameModifiersConfigs() {
        // xTranlator query = 'dn_Common'
        val inputFileName = "name.modifiers.json"
        val configFile = File("src/main/resources/$inputFileName")
        val configs: List<XTranslatorConfig> = OM.readValue(configFile, XTRANSLATOR_TYPE_REF)
        val inputFile = File("test_resources/namemods/$locale/namemods.xml")
        val values: Map<String, Fo76String> = parseXml(inputFile)

        configs.forEach { mod ->
            if (values.containsKey(mod.id)) {
                mod.texts[locale] = values[mod.id]?.source.toString()
            }
        }

        OM.writeValue(File(inputFileName), configs)
    }

    @Test
    fun ammoTypesConfigs() {
        // xTranlator query = 'AMMO:ONAM'
        val inputFileName = "ammo.types.json"
        val configFile = File("src/main/resources/$inputFileName")
        val configs: List<XTranslatorConfig> = OM.readValue(configFile, XTRANSLATOR_TYPE_REF)
        val inputFile = File("test_resources/ammotypes/$locale/ammotypes.xml")
        val values: Map<String, Fo76String> = parseXml(inputFile)

        configs.forEach { mod ->
            if (values.containsKey(mod.id)) {
                mod.texts[locale] = values[mod.id]?.source.toString()
            }
        }

        OM.writeValue(File(inputFileName), configs)
    }

    @Test
    fun testNukaCrypt() {
        val service = NukaCryptService(OM)
        val inputFileName = "legendaryMods.config.json"
        val configFile = File("src/main/resources/$inputFileName")
        val configs: List<LegendaryModDescriptor> = OM.readValue(configFile, LEG_MOD_TYPE_REF)
        configs.forEach { config ->
            run {
                config.gameId = getGameId(config.id, service)
            }
        }
        OM.writeValue(File(inputFileName), configs)
    }

    @Test
    fun validateLegConfigWithFed76() {
        val obj: Any = "null"
        val priceCheckRepository: PriceCheckRepository = (obj as PriceCheckRepository)
        val fed76Service = Fed76Service(priceCheckRepository, OM)
        val mapping = fed76Service.getMapping()
        val inputFileName = "legendaryMods.config.json"
        val configFile = File("src/main/resources/$inputFileName")
        val configs: List<LegendaryModDescriptor> = OM.readValue(configFile, LEG_MOD_TYPE_REF)
        val values = mapping.effects.byName.values
        for (config in configs) {
            var found = false
            val configId = config.id
            val gameId = config.gameId
            val text = config.texts["en"]?.replace(" ", "")?.replace("'", "")
            val abbreviation = config.abbreviation
            for (value in values) {
                val queries = value.queries
                val id = value.id
                for (query in queries) {
                    if (StringUtils.equalsIgnoreCase(query, abbreviation)) {
                        found = true
                        break
                    }
//                    val newQuery = query.replace(" ", "").replace("'", "")
//                    if (StringUtils.equalsIgnoreCase(newQuery, text)) {
//                        found = true
//                        if (!StringUtils.equalsIgnoreCase(gameId, id)) {
//                            println("Invalid gameId: $configId")
//                            config.gameId = id
//                            config.abbreviation = queries.minOrNull()
//                        }
//                        break
//                    } else {
//                        println()
//                    }
                }

            }
            if (!found) {
                println("Missing mapping for $configId")
            }
        }
        OM.writeValue(File(inputFileName), configs)
        println()
    }

    @Test
    fun convertModRequestToItemDescriptors() {
        val inDir: File = File("test_resources/testInventoryDumps/orig")
        val outDir: File = File("test_resources/testInventoryDumps/converted")
        for (origFile in inDir.listFiles()) {
            try {
                val modData = OM.readValue(origFile, ModData::class.java)
                val modDataRequest: ModDataRequest = ModDataRequest()
                modDataRequest.modData = modData
                modDataRequest.filters = ItemsUploadFilters()
//                modDataRequest.filters.tradableOnly = true
//                modDataRequest.filters.legendaryOnly = true
                val filterFlags: List<FilterFlag> = listOf(FilterFlag.ARMOR, FilterFlag.WEAPON, FilterFlag.WEAPON_RANGED, FilterFlag.WEAPON_MELEE)
                modDataRequest.filters.filterFlags = filterFlags.flatMap { x -> x.getFlags() }

                val list = itemService.prepareModData(modDataRequest)
                if (CollectionUtils.isNotEmpty(list)) {
                    OM.writeValue(File(outDir, "${origFile.nameWithoutExtension}.json"), list)
                }
            } catch (e: Exception) {
                println(origFile.name)
            }
        }
    }

    fun isCustomName(input: String): Boolean {
        return StringUtils.containsIgnoreCase(input, "+3")
    }

    @Test
    internal fun retrieveItemsGameIds() {
        val inDir: File = File("test_resources/testInventoryDumps/converted")
        val weaponNames: MutableSet<String> = HashSet()
        val armorNames: MutableSet<String> = HashSet()
        for (inputFile in inDir.listFiles()) {
            try {
                val items: List<ItemDTO> = OM.readValue(inputFile, ITEM_DTO_TYPE_REF)
                for (item in items) {
                    if (!item.isLegendary) {
                        continue
                    }
                    if (item.filterFlag === FilterFlag.WEAPON_MELEE || item.filterFlag === FilterFlag.WEAPON_RANGED) {
                        if (StringUtils.isBlank(item.itemDetails.name)) {
                            weaponNames.add(item.text!!)
                        } else {
                            weaponNames.add(item.itemDetails.name)
                        }
                        continue
                    }
                    if (item.filterFlag === FilterFlag.ARMOR) {
                        var name = item.itemDetails.name
                        if (StringUtils.isBlank(item.itemDetails.name)) {
                            name = item.text!!
                        }
                        if (!isCustomName(name)) {
                            armorNames.add(name)
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
        val nukaService = NukaCryptService(OM)
        val nukaRequest = NukaRequest()
        nukaRequest.setWeapon()
        val nukaRows: MutableList<NukaRow> = ArrayList()
        val emptyRows: MutableList<String> = ArrayList()
        val multipleRows: MutableList<String> = ArrayList()
        val noRows: MutableList<String> = ArrayList()
        for (name in weaponNames) {
            nukaRequest.searchtext = name
            val request = nukaService.performRequest(nukaRequest)
            if (request == null) {
                println("Failure for $name")
                continue
            }
            val rows: List<NukaRow> = request.rows
            var found = false
            for (row in rows) {
                if (StringUtils.equalsIgnoreCase(name, row.name)) {
                    found = true
                    nukaRows.add(row)
                    break
                }
            }
            if (rows.size > 1 && found) {
                multipleRows.add(name)
                continue
            }
            if (CollectionUtils.isEmpty(rows)) {
                emptyRows.add(name)
                continue
            }
            if (!found) {
                noRows.add(name)
            }
        }
        OM.writeValue(File("weaponConfigs.json"), nukaRows)
        println("Empty rows: $emptyRows")
        println("Multiple rows: $multipleRows")
        println("No rows: $noRows")
    }

    private fun getGameId(input: String, service: NukaCryptService): String? {
        if (StringUtils.isBlank(input)) {
            println("Empty input!")
            return ""
        }
        val request = NukaRequest()
        request.searchtext = input
        val nukaResponse = service.performRequest(request)
        if (nukaResponse === null || CollectionUtils.isEmpty(nukaResponse.rows)) {
            println("Null response for $input")
            return ""
        }
        nukaResponse.rows.forEach { row ->
            if (StringUtils.equalsIgnoreCase(input, row.edid)) {
                return row.formId
            }
        }
        println("ID not found for $input")
        return ""
    }
}