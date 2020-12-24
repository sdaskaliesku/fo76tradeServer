package com.manson.fo76.helper

import com.manson.fo76.config.AppConfig
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.User
import java.io.StringWriter
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.slf4j.LoggerFactory

object Utils {
    private val LOGGER = LoggerFactory.getLogger(Utils::class.java)

    fun validatePassword(user: User?, userInDb: User?): Boolean {
        return StringUtils.equals(user!!.password, userInDb!!.password)
    }

    fun areSameItems(first: ItemDTO, second: ItemDTO?): Boolean {
        val sameName = StringUtils.equalsIgnoreCase(first.text, second!!.text)
        val sameLevel = NumberUtils.compare(first.itemLevel, second.itemLevel) == 0
        val sameLegMods = StringUtils.equalsIgnoreCase(first.itemDetails.abbreviation, second.itemDetails.abbreviation)
        return sameName && sameLevel && sameLegMods
    }

    fun silentParse(value: String?): Number {
        try {
            return java.lang.Double.valueOf(value)
        } catch (ignored: Exception) {
        }
        return -1
    }

    fun listToCSV(input: List<Any?>?): String {
        val jsonString = AppConfig.objectMapper.writeValueAsString(input)
        val flatMe = JFlatCustom(jsonString)
        val stringWriter = StringWriter()
        flatMe
                .json2Sheet()
                .headerSeparator(".")
                .write2csv(stringWriter, ',')
        return stringWriter.toString()
    }
}