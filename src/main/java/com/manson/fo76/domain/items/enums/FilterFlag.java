package com.manson.fo76.domain.items.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public enum FilterFlag {
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
  JUNK("Junk", 33792, 1024);

  private final List<Integer> flags = new ArrayList<>();
  private final String name;
  private final boolean hasStarMods;

  FilterFlag(String name, boolean hasStarMods, Integer... flags) {
    this.flags.addAll(Arrays.asList(flags));
    this.name = name;
    this.hasStarMods = hasStarMods;
  }

  FilterFlag(String name, Integer... flags) {
    this(name, false, flags);
  }

  FilterFlag(String name, boolean hasStarMods, int flag) {
    this.flags.add(flag);
    this.name = name;
    this.hasStarMods = hasStarMods;
  }

  FilterFlag(String name, int flag) {
    this(name, false, flag);
  }

  public static FilterFlag fromInt(Integer flag) {
    if (flag == null) {
      return null;
    }
    return Arrays.stream(FilterFlag.values())
        .filter(filterFlag -> filterFlag.getFlags().contains(flag)).findFirst().orElse(null);
  }

  public static FilterFlag fromString(String flag) {
    if (flag == null) {
      return null;
    }
    return Arrays.stream(FilterFlag.values())
        .filter(filterFlag -> StringUtils.equalsIgnoreCase(flag, filterFlag.getName())).findFirst().orElse(null);
  }

  public boolean isHasStarMods() {
    return hasStarMods;
  }

  public List<Integer> getFlags() {
    return flags;
  }

  public String getName() {
    return name;
  }
}
