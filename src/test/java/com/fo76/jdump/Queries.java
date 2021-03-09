package com.fo76.jdump;

public class Queries {

  private static final String SELECT = "SELECT * from fo76jdump WHERE ";
  private static final String PLAYABLE_NAME_NOT_NULL = " and flags not like '%Non-Playable%' and name != 'null'";
  public static final String LEG_MODS = SELECT
      + "signature = \"OMOD\" and editorid like '%mod_Legendary%'";// + PLAYABLE_NAME_NOT_NULL;
  public static final String AMMO = SELECT + "signature = \"AMMO\"" + PLAYABLE_NAME_NOT_NULL;
  public static final String ARMO = SELECT + "signature = \"ARMO\"" + PLAYABLE_NAME_NOT_NULL;
  public static final String WEAPON = SELECT + "signature = 'WEAP'\n"
      + PLAYABLE_NAME_NOT_NULL
      + "  and editorid not like '%Dummy%'\n"
      + "  and editorid not like '%Test%'\n"
      + "  and editorid not like '%Temp%'\n"
      + "  and editorid not like 'POST%'\n"
      + "  and editorid not like 'CUT%'\n"
      + "  and editorid not like 'DEPRECATED%'\n"
      + "  and editorid not like 'Workshop%'\n"
      + "  and editorid not like 'zzz%'\n"
      + "  and editorid not like 'Survival%'\n"
      + "  and editorid not like 'V96%'\n"
      + "  and editorid not like '%DUPLICATE%'\n"
      + "  and editorid not like '%OBSOLETE%'\n"
      + "  and editorid not like 'DLC05WorkshopFireworkWeapon%'\n"
      + "  and editorid not like 'DELETED%'\n"
      + "  and editorid not like 'ATX_KDInkwell_Quill%'\n"
      + "  and editorid not like 'W05%'\n"
      + "  and editorid not like 'Debug%'\n"
      + "  and editorid not like '%Epic'\n"
      + "  and editorid not like 'ATX_CroquetMallet_Red%'\n"
      + "  and editorid not like '%Babylon%'\n"
      + "  and editorid not like 'MTR06%'\n"
      + "  and editorid not like 'MTR05_10mm_ScorchedTrainer%'\n"
      + "  and editorid not like 'P01B_Mini_Robot01_Binoculars%'";

  public static final String ARMOR = SELECT + "signature = 'ARMO'\n"
      + PLAYABLE_NAME_NOT_NULL
      + "  and editorid not like '%Dummy%'\n"
      + "  and editorid not like '%Test%'\n"
      + "  and editorid not like '%Temp%'\n"
      + "  and editorid not like 'POST%'\n"
      + "  and editorid not like 'CUT%'\n"
      + "  and editorid not like 'DEPRECATED%'\n"
      + "  and editorid not like 'Workshop%'\n"
      + "  and editorid not like 'zzz%'\n"
      + "  and editorid not like 'Survival%'\n"
      + "  and editorid not like 'V96%'\n"
      + "  and editorid not like '%DUPLICATE%'\n"
      + "  and editorid not like '%OBSOLETE%'\n"
      + "  and editorid not like 'DLC05WorkshopFireworkWeapon%'\n"
      + "  and editorid not like 'DELETED%'\n"
      + "  and editorid not like 'ATX_KDInkwell_Quill%'\n"
      + "  and editorid not like 'W05%'\n"
      + "  and editorid not like 'Debug%'\n"
      + "  and editorid not like '%Epic'\n"
      + "  and editorid not like 'ATX_CroquetMallet_Red%'\n"
      + "  and editorid not like '%Babylon%'\n"
      + "  and editorid not like 'MTR06%'\n"
      + "  and editorid not like 'MTR05_10mm_ScorchedTrainer%'\n"
      + "  and editorid not like 'P01B_Mini_Robot01_Binoculars%'";

  public static final String PLANS_RECIPES = SELECT
      + "(name like \"%plan%\" or name like \"%recipe%\")\n"
      + "  and signature = \"BOOK\"\n"
      + "  and editorid not like '%Dummy%'\n"
      + "  and editorid not like '%Test%'\n"
      + "  and editorid not like '%Temp%'\n"
      + "  and editorid not like 'POST%'\n"
      + "  and editorid not like 'CUT%'\n"
      + "  and editorid not like 'DEPRECATED%'\n"
      + "  and editorid not like 'Workshop%'\n"
      + "  and editorid not like 'zzz%'\n"
      + "  and editorid not like 'Survival%'\n"
      + "  and editorid not like 'V96%'\n"
      + "  and editorid not like '%DUPLICATE%'\n"
      + "  and editorid not like '%OBSOLETE%'\n"
      + "  and editorid not like 'DLC05WorkshopFireworkWeapon%'\n"
      + "  and editorid not like 'DELETED%'\n"
      + "  and editorid not like 'ATX_KDInkwell_Quill%'\n"
      + "  and editorid not like 'W05%'\n"
      + "  and editorid not like 'Debug%'\n"
      + "  and editorid not like '%Epic'\n"
      + "  and editorid not like 'ATX_CroquetMallet_Red%'\n"
      + "  and editorid not like '%Babylon%'\n"
      + "  and editorid not like 'MTR06%'\n"
      + "  and editorid not like 'MTR05_10mm_ScorchedTrainer%'\n"
      + "  and editorid not like 'P01B_Mini_Robot01_Binoculars%'";



}
