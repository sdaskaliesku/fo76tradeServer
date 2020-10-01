package com.manson.fo76.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.underscore.lodash.U
import com.manson.domain.config.Fo76String
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Consumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class XTranslatorParser(@Autowired private val objectMapper: ObjectMapper) {
    fun parse(file: File): List<Fo76String> {
        try {
            var xml = java.lang.String.join("\n", Files.readAllLines(Paths.get(file.toURI())))
            xml = xml.replace(xml[0].toString(), "")
            val map = U.fromXml<Map<String, Any>>(xml)
            val sstxmlRessources = map["SSTXMLRessources"] as Map<*, *>?
            val lang = (sstxmlRessources!!["Params"] as Map<*, *>?)!!["Dest"] as String?
            val strings = (sstxmlRessources["Content"] as Map<*, *>?)!!["String"] as List<*>
            val typeReference: TypeReference<List<Fo76String>> = object : TypeReference<List<Fo76String>>() {}
            val fo76Strings = objectMapper.convertValue(strings, typeReference)
            fo76Strings.forEach(Consumer { str: Fo76String -> str.lang = lang!! })
            return fo76Strings
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}