package com.manson.fo76.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.ArrayList
import java.util.HashMap
import org.apache.commons.lang3.StringUtils

@JsonIgnoreProperties(ignoreUnknown = true)
class LegendaryModDescriptor : XTranslatorConfig() {
    var star = 0
    var abbreviation: String? = null
    var additionalAbbreviations: List<String> = ArrayList()
    var translations: Map<String, String> = HashMap()
    var itemType: String? = null

    @JsonIgnore
    fun isTheSameMod(modName: String): Boolean {
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