package com.manson.fo76.domain.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.manson.domain.config.XTranslatorConfig
import java.util.ArrayList
import java.util.HashMap
import org.apache.commons.lang3.StringUtils

@JsonIgnoreProperties(ignoreUnknown = true)
class LegendaryModDescriptor : XTranslatorConfig() {
    var star = 0
    var abbreviation: String? = null
    var additionalAbbreviations: List<String> = ArrayList()
    var translations: MutableMap<String, String> = HashMap()
    var itemType: FilterFlag = FilterFlag.UNKNOWN

    @JsonIgnore
    fun isTheSameMod(modName: String, filterFlag: FilterFlag): Boolean {
        if (filterFlag != FilterFlag.UNKNOWN) {
            if (itemType != filterFlag && !filterFlag.subtypes.contains(itemType) && !itemType.subtypes.contains(filterFlag)) {
                return false
            }
        }
        val input = prepareString(modName)
        for (text in texts.values) {
            if (StringUtils.equalsIgnoreCase(prepareString(text), input)) {
                return true
            }
        }
        for (translation in translations.values) {
            if (StringUtils.equalsIgnoreCase(prepareString(translation), input)) {
                return true
            }
        }
        return false
    }

    companion object {
        private fun prepareString(input: String): String {
            return input.trim().replace("'", "").replace("+", "").replace(".", "").replace("¢", "")
        }
    }

}