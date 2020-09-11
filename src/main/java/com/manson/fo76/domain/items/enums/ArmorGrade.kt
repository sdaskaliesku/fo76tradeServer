package com.manson.fo76.domain.items.enums

import com.fasterxml.jackson.annotation.JsonValue
import org.apache.commons.lang3.StringUtils

enum class ArmorGrade(@get:JsonValue val value: String) {
    Light("Light"), Sturdy("Sturdy"), Heavy("Heavy"), Unknown("Unknown");

    companion object {
        public fun fromString(input: String?): ArmorGrade {
            if (StringUtils.isBlank(input)) {
                return Unknown
            }
            for (grade in values()) {
                if (StringUtils.equalsIgnoreCase(grade.value, input)) {
                    return grade
                }
            }
            return Unknown
        }
    }
}