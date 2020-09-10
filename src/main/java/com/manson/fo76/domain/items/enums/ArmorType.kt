package com.manson.fo76.domain.items.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class ArmorType(@get:JsonValue val value: String) {
    Light("Light"), Sturdy("Sturdy"), Heavy("Heavy"), Unknown("Unknown");
}