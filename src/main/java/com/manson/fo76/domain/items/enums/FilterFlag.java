package com.manson.fo76.domain.items.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public enum FilterFlag {
  POWER_ARMOR("Power Armor", 0),
  WEAPON("Weapon", 2, 3),
  ARMOR("Armor", 4),
  AID("Aid", 8, 9),
  HOLO("Holo", 512),
  AMMO("Ammo", 4096),
  NOTES("Notes", 128, 8192),
  MISC("Misc", 33280),
  MODS("Mods", 2048),
  JUNK("Junk", 33792, 1024);

  private final List<Integer> flags = new ArrayList<>();
  private final String name;

  FilterFlag(String name, Integer... flags) {
    this.flags.addAll(Arrays.asList(flags));
    this.name = name;
  }

  FilterFlag(String name, int flag) {
    this.flags.add(flag);
    this.name = name;
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

  public List<Integer> getFlags() {
    return flags;
  }

  public String getName() {
    return name;
  }
}
