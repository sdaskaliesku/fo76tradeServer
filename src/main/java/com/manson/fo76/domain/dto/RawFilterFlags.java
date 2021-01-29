package com.manson.fo76.domain.dto;

public class RawFilterFlags {
  public static final long FILTER_FAVORITES = 1 << 0;

  public static final long FILTER_WEAPONS = 1 << 2;

  public static final long FILTER_ARMOR = 1 << 3;

  public static final long FILTER_APPAREL = 1 << 4;

  public static final long FILTER_FOODWATER = 1 << 5;

  public static final long FILTER_AID = 1 << 6;

  public static final long FILTER_BOOKS = 1 << 10;

  public static final long FILTER_MISC = 1 << 12;

  public static final long FILTER_JUNK = 1 << 13;

  public static final long FILTER_MODS = 1 << 14;

  public static final long FILTER_AMMO = 1 << 15;

  public static final long FILTER_HOLOTAPES = 1 << 16;

  public static final long FILTER_CANAUTOSCRAP = 1 << 18;

  public static final long FILTER_ALL = 4294967295L;
}
