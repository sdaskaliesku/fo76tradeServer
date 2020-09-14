package com.manson.fo76.domain.items.enums

import java.util.ArrayList
import java.util.Arrays
import org.apache.commons.lang3.StringUtils

enum class FilterFlag {
    POWER_ARMOR("Power Armor", 0),
    WEAPON_MELEE("Weapon - Melee", true),
    WEAPON_RANGED("Weapon - Ranged", true),
    WEAPON_THROWN("Weapon - Thrown"),
    WEAPON("Weapon", true, listOf(WEAPON_THROWN, WEAPON_MELEE, WEAPON_RANGED), 2, 3),
    ARMOR_OUTFIT("Armor - Outfit"),
    ARMOR("Armor", true, listOf(ARMOR_OUTFIT), 4),
    AID("Aid", 8, 9),
    HOLO("Holo", 512),
    AMMO("Ammo", 4096),
    NOTES("Notes", 128, 8192),
    MISC("Misc", 33280),
    MODS("Mods", 2048),
    JUNK("Junk", 33792, 1024),
    UNKNOWN("Unknown");

    private val flags: MutableList<Int> = ArrayList()
    val value: String
    val isHasStarMods: Boolean
    val subtypes: List<FilterFlag>

    constructor(name: String, hasStarMods: Boolean, subtypes: List<FilterFlag> = listOf(), vararg flags: Int) {
        this.flags.addAll(flags.asList())
        this.value = name
        this.isHasStarMods = hasStarMods
        this.subtypes = subtypes
    }

    constructor(name: String, vararg flags: Int) : this(name, hasStarMods = false, flags = flags)
//    constructor(name: String, hasStarMods: Boolean, flag: Int) {
//        flags.add(flag)
//        this.value = name
//        isHasStarMods = hasStarMods
//    }

//    constructor(name: String, flag: Int) : this(name, false, flag)

    fun getFlags(): List<Int> {
        return flags
    }

    companion object {
        @JvmStatic
        fun fromInt(flag: Int?): FilterFlag {
            return if (flag == null) {
                UNKNOWN
            } else Arrays.stream(values())
                    .filter { filterFlag: FilterFlag -> filterFlag.getFlags().contains(flag) }.findFirst().orElse(UNKNOWN)
        }

        fun fromString(flag: String?): FilterFlag {
            return if (StringUtils.isBlank(flag)) {
                UNKNOWN
            } else Arrays.stream(values())
                    .filter { filterFlag: FilterFlag -> StringUtils.equalsIgnoreCase(flag, filterFlag.value) }.findFirst().orElse(UNKNOWN)
        }
    }
}