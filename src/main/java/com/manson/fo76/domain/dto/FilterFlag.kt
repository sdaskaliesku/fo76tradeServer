package com.manson.fo76.domain.dto

import java.util.Arrays
import org.apache.commons.lang3.StringUtils

enum class FilterFlag {
    POWER_ARMOR("Power Armor", 0),
    WEAPON_MELEE("Weapon - Melee", true),
    WEAPON_RANGED("Weapon - Ranged", true),
    WEAPON_THROWN("Weapon - Thrown"),
    WEAPON("Weapon", true, listOf(WEAPON_THROWN, WEAPON_MELEE, WEAPON_RANGED), RawFilterFlags.FILTER_WEAPONS, 5),
    ARMOR("Armor", true, listOf(), RawFilterFlags.FILTER_ARMOR),
    APPAREL("Apparel", RawFilterFlags.FILTER_APPAREL),
    FOOD_WATER("Food - Water", RawFilterFlags.FILTER_FOODWATER, 33),
    AID("Aid", RawFilterFlags.FILTER_AID, 65),
    NOTES("Notes", RawFilterFlags.FILTER_BOOKS),
    HOLO("Holo", RawFilterFlags.FILTER_HOLOTAPES),
    AMMO("Ammo", RawFilterFlags.FILTER_AMMO),
    MISC("Misc", RawFilterFlags.FILTER_MISC, 266240),
    MODS("Mods", RawFilterFlags.FILTER_MODS),
    JUNK("Junk", RawFilterFlags.FILTER_JUNK, 270336),
    UNKNOWN("Unknown");

    private val flags: MutableList<Long> = ArrayList()
    val value: String
    val isHasStarMods: Boolean
    val subtypes: List<FilterFlag>

    constructor(name: String, hasStarMods: Boolean, subtypes: List<FilterFlag> = listOf(), vararg flags: Long) {
        this.flags.addAll(flags.asList())
        this.value = name
        this.isHasStarMods = hasStarMods
        this.subtypes = subtypes
    }

    constructor(name: String, vararg flags: Long) : this(name, hasStarMods = false, flags = flags)

    fun getFlags(): List<Long> {
        return flags.toList()
    }

    companion object {
        @JvmStatic
        fun fromInt(flag: Long?): FilterFlag {
            return if (flag == null) {
                UNKNOWN
            } else Arrays.stream(values())
                .filter { filterFlag: FilterFlag -> filterFlag.getFlags().contains(flag) }.findFirst().orElse(
                    UNKNOWN
                )
        }

        @JvmStatic
        fun fromString(flag: String?): FilterFlag {
            return if (StringUtils.isBlank(flag)) {
                UNKNOWN
            } else Arrays.stream(values())
                .filter { filterFlag: FilterFlag -> StringUtils.equalsIgnoreCase(flag, filterFlag.value) }.findFirst().orElse(
                    UNKNOWN
                )
        }
    }
}