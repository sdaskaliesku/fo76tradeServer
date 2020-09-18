package com.manson.fo76.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.config.AppConfig
import com.manson.fo76.domain.ModData
import com.manson.fo76.domain.fed76.Fed76ItemDto
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.StatsDTO
import java.io.File
import org.slf4j.LoggerFactory

object JsonParser {
    private val LOGGER = LoggerFactory.getLogger(JsonParser::class.java)
    private val TYPE_REFERENCE: TypeReference<MutableMap<String, Any?>> = object : TypeReference<MutableMap<String, Any?>>() {}
    private val OM: ObjectMapper = AppConfig.objectMapper
    fun objectToMap(o: Any?): MutableMap<String, Any?>? {
        try {
            return OM.convertValue(o, TYPE_REFERENCE)
        } catch (e: Exception) {
            LOGGER.error("Error converting object to map: {}", o, e)
        }
        return null
    }

    fun mapToItemDTO(map: MutableMap<String, Any?>?): ItemDTO? {
        return mapToClass(map, ItemDTO::class.java)
    }

    fun <T> mapToClass(map: MutableMap<String, Any?>?, clazz: Class<T>): T? {
        try {
            return OM.convertValue(map, clazz)
        } catch (e: Exception) {
            LOGGER.error("Converting map to class: {}", map, e)
        }
        return null
    }

    fun mapToStatsDTO(map: MutableMap<String, Any?>?): StatsDTO? {
        return mapToClass(map, StatsDTO::class.java)
    }

    fun parse(file: File?): ModData? {
        try {
            return OM.readValue(file, ModData::class.java)
        } catch (e: Exception) {
            LOGGER.error("Error parsing file", e)
        }
        return null
    }

    fun convertToFedItemDTO(itemDTO: ItemDTO): Fed76ItemDto? {
        val map = objectToMap(itemDTO)
        return mapToClass(map, Fed76ItemDto::class.java)
    }
}