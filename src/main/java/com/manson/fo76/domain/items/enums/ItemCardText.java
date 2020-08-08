package com.manson.fo76.domain.items.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public enum ItemCardText {
	DR("Damage reduction", "$dr"),
	WEIGHT("Weight", "$wt"),
	VAL("Value", "$val"),
	DESC("Description", "DESC"),
	LEG_MODS("Legendary Mods", "legendaryMods"),
	PERCEPTION("Perception", "PER"),
	INT("Intelligence", "INT"),
	STR("Strength", "STR"),
	END("Endurance", "END"),
	AGI("Agility", "AGI"),
	CHR("Charisma", "CHR"),
	LCK("Luck", "LCK"),
	ROF("Rate of fire", "$ROF"),
	RNG("Range", "$rng"),
	ACC("Accuracy", "$acc"),
	DMG("Damage", "$dmg"),
	FOOD("Food", "$Food"),
	WATER("Water", "$Water"),
	RADS("Rads", "Rads"),
	HP("HP", "HP"),
	RAD_RESIST("Rad Resist", "Rad Resist"),
	MOVE_SPEED("Move Speed", "Move Speed"),
	DISEASE_CHANCE("Disease Chance", "Disease Chance"),
	AP("Action point", "AP"),
	MELEE_CRIT_DMG("Melee Crit Dmg", "Melee Crit Dmg"),
	BAT_CHARGE("Battery charge", "$charge"),
	POISON_RESIST("Poison resist", "Poison Resist"),
	DMG_RESIST("Damage resist", "DMG Resist"),
	RAD_INGESTION("Rad Ingestion", "Rad Ingestion"),
	MELEE_SPEED("Melee speed", "$speed"),
	BONUS_XP("Bonus XP", "Bonus XP"),
	DURABILITY("Health/Durability", "$health"),
	HP_REGEN("Health Regen", "Health Regen"),
	AP_REGEN("AP Regen", "AP Regen"),
	DMG_VS_YAO("Dmg Vs Yao Guai", "Dmg Vs Yao Guai"),
	MAX_HP("Max HP", "Max HP"),
	DIS_RES("Disease Resist", "Disease Resist"),
	AMMO("Ammo", "5.56", "5mm", ".45", "Camera Film", "Core", "Ultracite Plasma Core",
			"Ultracite Fusion Core", "Shell", "Flare", "Fuel", ".38", "Arrow", "Harpoon", "Cryo",
			"Plasma", "Ultracite 5mm", "2mm EC", ".308", "Gamma Round", "AB Round", "Cell", ".44",
			".50 Ball", ".50", "Cannonball", "40mm", "10mm", "Missile"),
	CARRY_WEIGHT("Carry Weight", "Carry Weight");

	private final List<String> values = new ArrayList<>();
	private final String name;

	ItemCardText(String name, String... values) {
		this.values.addAll(Arrays.asList(values));
		this.name = name;
	}

	ItemCardText(String name, String value) {
		this.values.add(value);
		this.name = name;
	}

	public static ItemCardText fromString(String input) {
		if (StringUtils.isBlank(input)) {
			return null;
		}
		for (ItemCardText item : ItemCardText.values()) {
			for (String val : item.getValues()) {
				if (StringUtils.equalsIgnoreCase(val, input)) {
					return item;
				}
			}
		}
		return null;
	}

	public List<String> getValues() {
		return values;
	}

	@JsonValue
	public String getName() {
		return name;
	}
}
