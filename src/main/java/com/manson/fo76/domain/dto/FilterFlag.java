package com.manson.fo76.domain.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public enum FilterFlag {
  POWER_ARMOR("Power Armor", 0L),
  WEAPON_MELEE("Weapon - Melee", true),
  WEAPON_RANGED("Weapon - Ranged", true),
  WEAPON_THROWN("Weapon - Thrown"),
  WEAPON("Weapon", true, Arrays.asList(WEAPON_THROWN, WEAPON_MELEE, WEAPON_RANGED), RawFilterFlags.FILTER_WEAPONS, 5L),
  ARMOR("Armor", true, Collections.emptyList(), RawFilterFlags.FILTER_ARMOR),
  APPAREL("Apparel", RawFilterFlags.FILTER_APPAREL),
  FOOD_WATER("Food - Water", RawFilterFlags.FILTER_FOODWATER, 33L),
  AID("Aid", RawFilterFlags.FILTER_AID, 65L),
  NOTES("Notes", RawFilterFlags.FILTER_BOOKS),
  HOLO("Holo", RawFilterFlags.FILTER_HOLOTAPES),
  AMMO("Ammo", RawFilterFlags.FILTER_AMMO),
  MISC("Misc", RawFilterFlags.FILTER_MISC, 266240L),
  MODS("Mods", RawFilterFlags.FILTER_MODS),
  JUNK("Junk", RawFilterFlags.FILTER_JUNK, 270336L),
  UNKNOWN("Unknown");

  private final List<Long> flags = new ArrayList<>();
  private final String value;
  private final boolean hasStarMods;
  private final List<FilterFlag> subtypes;

  FilterFlag(String name, boolean hasStarMods, List<FilterFlag> subtypes, Long... flags) {
    this.flags.addAll(Arrays.asList(flags));
    this.value = name;
    this.hasStarMods = hasStarMods;
    this.subtypes = subtypes;
  }

  FilterFlag(String name, Long... flags) {
    this(name, false, Collections.emptyList(), flags);
  }

  FilterFlag(String name, boolean hasStarMods) {
    this(name, hasStarMods, Collections.emptyList());
  }

  public static FilterFlag fromInt(Long flag) {
    if (flag == null) {
      return UNKNOWN;
    }
    return Arrays.stream(values())
        .filter(filterFlag -> filterFlag.getFlags().contains(flag)).findFirst().orElse(UNKNOWN);
  }

  public static FilterFlag fromString(String flag) {
    if (StringUtils.isBlank(flag)) {
      return null;
    }
    return Arrays.stream(values())
        .filter(filterFlag -> StringUtils.equalsIgnoreCase(flag, filterFlag.value) || StringUtils.equalsIgnoreCase(flag, filterFlag.name())).findFirst().orElse(null);
  }

  public List<Long> getFlags() {
    return flags;
  }

  public String getValue() {
    return value;
  }

  public boolean isHasStarMods() {
    return hasStarMods;
  }

  public List<FilterFlag> getSubtypes() {
    return subtypes;
  }
}
