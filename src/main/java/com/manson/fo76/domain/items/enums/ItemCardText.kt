package com.manson.fo76.domain.items.enums

import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

enum class ItemCardText {
    DR("Damage reduction", "\$dr"),
    WEIGHT("Weight", "\$wt"),
    VAL("Value", "\$val"),
    DESC("Description", "DESC"),
    LEG_MODS("Legendary Mods", "legendaryMods"),
    PERCEPTION("Perception", "PER"),
    INT("Intelligence", "INT"),
    STR("Strength", "STR"),
    END("Endurance", "END"),
    AGI("Agility", "AGI"),
    CHR("Charisma", "CHR"),
    LCK("Luck", "LCK"),
    ROF("Rate of fire", "\$ROF"),
    RNG("Range", "\$rng"),
    ACC("Accuracy", "\$acc"),
    DMG("Damage", "\$dmg"),
    FOOD("Food", "\$Food"),
    WATER("Water", "\$Water"),
    RADS("Rads", "Rads"),
    HP("HP", "HP"),
    RAD_RESIST("Rad Resist", "Rad Resist"),
    MOVE_SPEED("Move Speed", "Move Speed"),
    DISEASE_CHANCE("Disease Chance", "Disease Chance"),
    AP("Action point", "AP"),
    MELEE_CRIT_DMG("Melee Crit Dmg", "Melee Crit Dmg"),
    BAT_CHARGE("Battery charge", "\$charge"),
    POISON_RESIST("Poison resist", "Poison Resist"),
    DMG_RESIST("Damage resist", "DMG Resist"),
    RAD_INGESTION("Rad Ingestion", "Rad Ingestion"),
    MELEE_SPEED("Melee speed", "\$speed"),
    BONUS_XP("Bonus XP", "Bonus XP"),
    DURABILITY("Health/Durability", "\$health"),
    HP_REGEN("Health Regen", "Health Regen"),
    AP_REGEN("AP Regen", "AP Regen"),
    DMG_VS_YAO("Dmg Vs Yao Guai", "Dmg Vs Yao Guai"),
    MAX_HP("Max HP", "Max HP"),
    MAX_AP("Max AP", "Max AP"),
    DIS_RES("Disease Resist", "Disease Resist"),
    AMMO("Ammo"),
    CARRY_WEIGHT("Carry Weight", "Carry Weight"),
    UNKNOWN("", "");

    private val values: MutableList<String> = ArrayList()

    @get:JsonValue
    val value: String

    constructor(name: String, vararg values: String) {
        this.values.addAll(Arrays.asList(*values))
        this.value = name
    }

    constructor(name: String, value: String) {
        values.add(value)
        this.value = name
    }

    fun getValues(): List<String> {
        return values
    }
}