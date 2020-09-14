import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.domain.ArmorConfig
import com.manson.fo76.domain.Fo76String
import com.manson.fo76.domain.LegendaryModDescriptor
import com.manson.fo76.domain.XTranslatorConfig
import com.manson.fo76.domain.XTranslatorConfig.Companion.merge
import com.manson.fo76.domain.items.enums.ArmorGrade
import com.manson.fo76.service.XTranslatorParser
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList
import java.util.HashMap
import java.util.stream.Collectors
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Test

class PopulateLegModsConfig {
    @Test
    @Throws(IOException::class)
    fun dummy2() {
        val input = File("./test_resources/ammo_types_ru.xml")
        val output = File("ammo.types.json")
        val input1 = File("D:\\workspace\\fo76tradeServer\\src\\main\\resources\\ammo.types.json")
        val typeReference: TypeReference<List<XTranslatorConfig>> = object : TypeReference<List<XTranslatorConfig>>() {}
        val xTranslatorConfigs = OM.readValue(input1, typeReference)
        val fo76Strings = xTranslatorParser.parse(input)
        val configs = fo76Strings.stream().map { obj: Fo76String -> XTranslatorConfig.fromFo76String(obj) }
                .collect(Collectors.toList())
        for (config in configs) {
            for (initial in xTranslatorConfigs) {
                merge(initial, config!!)
            }
        }
        OM.writeValue(output, xTranslatorConfigs)
    }

    fun shouldIgnoreConfig(config: XTranslatorConfig): Boolean {
        val values = config.texts.values
        for (value in values) {
            if (StringUtils.isBlank(value)) {
                return true
            }
            for (ignored in fullyIgnoredKeyWords) {
                if (StringUtils.equalsIgnoreCase(value, ignored)) {
                    return true
                }
            }
            for (ignored in ignoredKeyWords) {
                if (StringUtils.containsIgnoreCase(value, ignored)) {
                    return true
                }
            }
        }
        return false
    }

    private val ignoredKeyWords = listOf("rifle", "harpoon", "Napalmer", "Headlamp", "Revolver", "Shotgun", "Pistol", "Gun", "Slug Buster", "Plasma", "Cryo", "The Fixer", "Shi", "Double", "Handmade", "*", "Assault", "Flame", "The", "Mole miner")
    private val fullyIgnoredKeyWords = listOf("black", "light", "heavy")

    @Test
    internal fun nameModifiers() {
        val input = File("./test_resources/name_modifiers_en.xml")
        val output = File("name.modifiers.json")
        val fo76Strings = xTranslatorParser.parse(input)
        val configs = fo76Strings.stream().map { obj: Fo76String -> XTranslatorConfig.fromFo76String(obj) }
                .collect(Collectors.toList())
        val filtered: ArrayList<XTranslatorConfig> = ArrayList<XTranslatorConfig>()
        val ignoredIds: ArrayList<String> = ArrayList()
        for (config in configs) {
            if (!shouldIgnoreConfig(config)) {
                filtered.add(config)
            } else {
                config.sid?.let { ignoredIds.add(it) }
            }
        }
        // TODO: put "deep pocketed" BEFORE "pocketed" !!!
        filtered.sortByDescending { it.texts.values.first().length }
        OM.writeValue(output, filtered)
        OM.writeValue(File("ignored.name.modifiers.ids.json"), ignoredIds)
    }


    @Test
    @Throws(Exception::class)
    fun readArmorDr() {
        val input = File("./test_resources/armor_dr.txt")
        val strings = Files.readAllLines(Paths.get(input.toURI()))
        strings.removeAt(0)
        val configs: MutableList<ArmorConfig> = ArrayList()
        for (line in strings) {
            val config = line.split("\t").toTypedArray()
            var col = 0
            val armorDrConfig = ArmorConfig()
            armorDrConfig.helper = config[col++]
            armorDrConfig.dr = config[col++].toInt()
            armorDrConfig.er = config[col++].toInt()
            armorDrConfig.rr = config[col++].toInt()
            armorDrConfig.shortTerm = config[col++]
            armorDrConfig.armorType = config[col++]
            armorDrConfig.armorPart = config[col++]
            armorDrConfig.armorGrade = ArmorGrade.fromString(config[col++])
            col++ // do no use this field
            armorDrConfig.armorId = config[col++]
            armorDrConfig.gradeId = config[col++]

            configs.add(armorDrConfig)
        }
        OM.writeValue(File("armor.config.json"), configs)
    }

    @Test
    @Throws(IOException::class)
    fun dummy() {
        val baseDir = File("./test_resources")
        val input = File("D:\\workspace\\fo76tradeServer\\src\\main\\resources\\legendaryMods.config.json")
        val descriptors = OM.readValue(input, TYPE_REFERENCE)
        val keysFile = File(baseDir, "leg_mods_en_key.xml")
        val valuesFile = File(baseDir, "leg_mods_en_value.xml")
        val keys = parseXml(keysFile)
        val values = parseXml(valuesFile)
        for (descriptor in descriptors) {
            if (!keys.containsKey(descriptor.id) || !values.containsKey(descriptor.id)) {
                continue
            }
            val k = keys[descriptor.id]
            val v = values[descriptor.id]
            descriptor.texts[k!!.lang] = k.source!!
//            descriptor.translations.put(v!!.lang, v.source)
            descriptor.id = k.edid
            descriptor.sid = k.sid
            descriptor.rec = k.rec.toString()
        }
        OM.writeValue(File("legModsconfig2.json"), descriptors)
    }

    companion object {
        private val OM = ObjectMapper()
        private val xTranslatorParser = XTranslatorParser(OM)
        private val TYPE_REFERENCE: TypeReference<List<LegendaryModDescriptor>> = object : TypeReference<List<LegendaryModDescriptor>>() {}
        private fun parseXml(file: File): Map<String, Fo76String> {
            val strings: List<Fo76String> = xTranslatorParser.parse(file)
            return genericListToFo76List(strings)
        }

        fun genericListToFo76List(fo76Strings: List<Fo76String>): Map<String, Fo76String> {
            var map: Map<String, Fo76String> = HashMap()
            try {
                map = fo76Strings.stream().collect(Collectors.toMap(Fo76String::edid, { fo76String: Fo76String -> fo76String }) { a: Fo76String, b: Fo76String -> b })
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return map
        }
    }
}