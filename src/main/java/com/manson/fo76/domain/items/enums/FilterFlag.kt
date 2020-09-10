package com.manson.fo76.domain.items.enums

import org.apache.commons.lang3.StringUtils
import java.util.*

enum class FilterFlag {
    POWER_ARMOR("Power Armor", 0),
    WEAPON("Weapon", true, 2, 3),
    WEAPON_MELEE("Weapon - Melee", true),
    WEAPON_RANGED("Weapon - Ranged", true),
    WEAPON_THROWN("Weapon - Thrown"),
    ARMOR("Armor", true, 4),
    ARMOR_OUTFIT("Armor - Outfit"),
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

    constructor(name: String, hasStarMods: Boolean, vararg flags: Int) {
        this.flags.addAll(flags.asList())
        this.value = name
        isHasStarMods = hasStarMods
    }

    constructor(name: String, vararg flags: Int) : this(name, false, *flags)
    constructor(name: String, hasStarMods: Boolean, flag: Int) {
        flags.add(flag)
        this.value = name
        isHasStarMods = hasStarMods
    }

    constructor(name: String, flag: Int) : this(name, false, flag)

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