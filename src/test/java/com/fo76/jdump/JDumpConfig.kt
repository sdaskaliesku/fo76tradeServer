package com.fo76.jdump

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.domain.config.LegendaryModDescriptor
import com.manson.domain.config.XTranslatorConfig
import com.manson.domain.fo76.items.enums.FilterFlag
import com.manson.domain.itemextractor.ItemConfig
import java.io.File
import java.util.function.Function
import org.apache.commons.lang3.StringUtils
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.Test


class JDumpConfig {

    companion object {
        private val objectMapper = ObjectMapper()
        private var sessionFactory: SessionFactory?
        private val typeReference: TypeReference<MutableMap<String, String>> =
            object : TypeReference<MutableMap<String, String>>() {}
        private val LEG_MOD_TYPE_REF: TypeReference<List<LegendaryModDescriptor>> =
            object : TypeReference<List<LegendaryModDescriptor>>() {}
        private val XTRANSLATOR_TYPE_REF: TypeReference<List<XTranslatorConfig>> =
            object : TypeReference<List<XTranslatorConfig>>() {}

        init {
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)

            val config = Configuration()
            config.configure()
            sessionFactory = config.buildSessionFactory()
        }
    }

    private fun getData(query: String): MutableList<SqlLiteEntity> {
        var session: Session? = null
        var tx: Transaction? = null
        try {
            session = sessionFactory?.openSession()
            tx = session?.beginTransaction()
            val list = session
                ?.createNativeQuery(query)
                ?.addEntity(SqlLiteEntity::class.java)
                ?.list()
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(list)) {
                return list as MutableList<SqlLiteEntity>
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            session?.flush()
            tx?.commit()
        }
        return ArrayList()
    }

    fun getItemType(legMod: LegendaryModDescriptor): FilterFlag {
        if (StringUtils.containsIgnoreCase(legMod.id, "mod_Legendary_Armor")) {
            return FilterFlag.ARMOR
        } else if (StringUtils.containsIgnoreCase(legMod.id, "mod_Legendary_Weapon")) {
            if (StringUtils.containsIgnoreCase(legMod.id, "guns")) {
                return FilterFlag.WEAPON_RANGED
            } else if (StringUtils.containsIgnoreCase(legMod.id, "melee")) {
                return FilterFlag.WEAPON_MELEE
            } else {
                return FilterFlag.WEAPON
            }
        }
        return FilterFlag.UNKNOWN
    }

    fun <T> dumpSqlToJson(
        outputFile: File,
        query: String,
        callback: Function<SqlLiteEntity, T>
    ): List<T> {
        val list: MutableList<T> = ArrayList()
        getData(query).forEach {
            val result: T = callback.apply(it)
            list.add(result)
        }
        objectMapper.writeValue(outputFile, list)
        return list
    }

    @Test
    fun dumpLegendaryMods() {
        // LEGENDARY MODS: signature = "OMOD" and editorid like '%mod_Legendary%' and flags != '"Non-Playable"' and name != 'null'
        // AMMO: signature = "AMMO" and flags != '"Non-Playable"'  and name != 'null'
        // ARMO: signature = "ARMO" and flags != '"Non-Playable"' and name != "null"

        val outputFile = File("dumpLegendaryMods.json")
        val mainConfigFile = File("src/main/resources/legendaryMods.config.json")
        val newModList: MutableList<LegendaryModDescriptor> = ArrayList()
        val legMods: MutableList<LegendaryModDescriptor> =
            objectMapper.readValue(
                mainConfigFile,
                LEG_MOD_TYPE_REF
            ) as MutableList<LegendaryModDescriptor>
        getData(Queries.LEG_MODS).forEach {
            run {
                val sqlLiteEntity = it;
                legMods.forEach {
                    run {
                        if (StringUtils.equalsIgnoreCase(it.id, sqlLiteEntity.editorid)) {
                            it.gameId = sqlLiteEntity.formid
                        }
                    }
                }
//                val legMod = LegendaryModDescriptor()
//                legMod.texts = objectMapper.readValue(it.name, typeReference)
//                legMod.translations = objectMapper.readValue(it.desc, typeReference)
//                legMod.id = it.editorid
//                legMod.gameId = it.formid
//                val starStr = legMod.id.filter { it.isDigit() }
//                if (StringUtils.isNotBlank(starStr)) {
//                    legMod.star = Integer.parseInt(starStr)
//                }
//                legMod.itemType = getItemType(legMod)
//                if (legMod.star == 0) {
//                    legMod.enabled = false
//                }
//                newModList += legMod
            }
        }
//        objectMapper.writeValue(outputFile, newModList)
//
//        val legMods: MutableList<LegendaryModDescriptor> =
//            objectMapper.readValue(
//                mainConfigFile,
//                LEG_MOD_TYPE_REF
//            ) as MutableList<LegendaryModDescriptor>
//        val list = objectMapper.readValue(outputFile, LEG_MOD_TYPE_REF)
//        list.forEach {
//            run {
//                var found = false;
//                legMods.forEach { dumpLeg ->
//                    run {
//                        dumpLeg.itemType = getItemType(dumpLeg)
//                        if (StringUtils.equalsIgnoreCase(it.gameId, dumpLeg.gameId)) {
//                            dumpLeg.translations = it.translations
//                            dumpLeg.texts = it.texts
//                            found = true
//                        }
//                    }
//                }
//                if (!found) {
//                    legMods.add(it);
//                }
//            }
//        }
        objectMapper.writeValue(mainConfigFile, legMods)
    }

    @Test
    fun dumpAmmo() {
        val outputFile = File("dumpAmmo.json")
        val mainConfigFile = File("src/main/resources/ammo.types.json")
        val ammoConfig: MutableList<XTranslatorConfig> = ArrayList()
        getData(Queries.AMMO).forEach {
            run {
                val xConf = XTranslatorConfig()
                xConf.texts = objectMapper.readValue(it.name, typeReference)
                xConf.id = it.editorid
                xConf.gameId = it.formid
                ammoConfig.add(xConf)
            }
        }
        val ammos: MutableList<XTranslatorConfig> =
            objectMapper.readValue(
                mainConfigFile,
                XTRANSLATOR_TYPE_REF
            ) as MutableList<XTranslatorConfig>

        ammoConfig.forEach {
            run {
                var found = false
                ammos.forEach { oldAmmo ->
                    run {
                        if (StringUtils.equalsIgnoreCase(it.gameId, oldAmmo.gameId)) {
                            oldAmmo.texts = it.texts
                            found = true
                        }
                    }
                }
                if (!found) {
                    ammos.add(it)
                }
            }
        }
        objectMapper.writeValue(mainConfigFile, ammos)
        objectMapper.writeValue(outputFile, ammoConfig)
    }

    @Test
    fun dumpArmo() {
        getData(Queries.ARMO).forEach {
            run {
                println(it)
            }
        }
    }

    @Test
    fun dumpWeaponNames() {
        val sqlData: MutableList<SqlLiteEntity> = getData(Queries.WEAPON)
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        val data: MutableList<ItemConfig> = ArrayList()
        sqlData.forEach {
            run {
                val itemConfig = ItemConfig()
                val name = objectMapper.readValue(it.name, typeReference)
                itemConfig.gameId = it.formid
                itemConfig.id = it.editorid
                itemConfig.texts = name
                if (StringUtils.containsIgnoreCase(it.jdump, "WeaponTypeGrenade")) {
                    itemConfig.type = FilterFlag.WEAPON_THROWN
                } else if (StringUtils.containsIgnoreCase(it.jdump, "dn_CommonGun")) {
                    itemConfig.type = FilterFlag.WEAPON_RANGED
                } else if (StringUtils.containsIgnoreCase(it.jdump, "dn_CommonMelee")) {
                    itemConfig.type = FilterFlag.WEAPON_MELEE
                } else {
                    itemConfig.type = FilterFlag.WEAPON
                }
                itemConfig.isEnabled = true
                data.add(itemConfig)
            }
        }
        val file = File("armor_weapon_names.json")
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data)
    }

    @Test
    fun dumpArmorNames() {
        val sqlData: MutableList<SqlLiteEntity> = getData(Queries.ARMOR)
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        val data: MutableList<ItemConfig> = ArrayList()
        sqlData.forEach {
            run {
                val itemConfig = ItemConfig()
                val name = objectMapper.readValue(it.name, typeReference)
                itemConfig.gameId = it.formid
                itemConfig.id = it.editorid
                itemConfig.texts = name
                itemConfig.type = FilterFlag.ARMOR
                itemConfig.isEnabled = true
                data.add(itemConfig)
            }
        }
        val file = File("armor.config.json")
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data)
    }

    @Test
    internal fun dumpPlanRecipesNames() {
        val sqlData: MutableList<SqlLiteEntity> = getData(Queries.PLANS_RECIPES)
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        val data: MutableList<ItemConfig> = ArrayList()
        sqlData.forEach {
            run {
                val itemConfig = ItemConfig()
                val name = objectMapper.readValue(it.name, typeReference)
                itemConfig.gameId = it.formid
                itemConfig.id = it.editorid
                itemConfig.texts = name
                itemConfig.type = FilterFlag.NOTES
                itemConfig.isEnabled = true
                data.add(itemConfig)
            }
        }
        val file = File("plans.config.json")
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data)
    }
}