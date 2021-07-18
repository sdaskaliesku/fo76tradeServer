import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.fo76.items.enums.FilterFlag;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Fo76DumpTest {

  private static final String PATH_TO_FO76_DUMP = "C:\\fo76tools\\jdump\\c82fbd7b9b21b405a4bf92d123fe4c04c402d90c";

  private static final List<String> SIGNATURES = Arrays
      .asList(
          "ARMO", "WEAP", "AMMO", "OMOD", "ENCH", "MGEF", "COBJ", "NOTE", "BOOK", "MISC", "CONT", "FLOR", "NPC_",
          "RACE", "MSWP", "STAT", "KYWD", "LVLI", "UTIL"
      );
  private static final ObjectMapper OM = new ObjectMapper();
  private static final TypeReference<Map<String, Object>> HUD_EDITOR_CONFIG = new TypeReference<Map<String, Object>>() {
  };
  private static final TypeReference<Map<String, Map<String, String>>> STRINGS = new TypeReference<Map<String, Map<String, String>>>() {
  };
  private static final TypeReference<List<LegendaryModDescriptor>> LEG_MOD_TYPE_REF = new TypeReference<List<LegendaryModDescriptor>>() {
  };

  private static final String STRINGS_JSON = "strings.json";
  private static final String LEG_MODS_FILE = "src/main/resources/configs/legendaryMods.config.json";
  private static Map<String, Map<String, String>> translationsMap;

  @BeforeAll
  public static void beforeAll() throws IOException {
    OM.setSerializationInclusion(Include.NON_EMPTY);
    File file = new File(PATH_TO_FO76_DUMP, STRINGS_JSON);
    translationsMap = OM.readValue(file, STRINGS);
  }

  private static File buildJsonFile(String signature) {
    File internationalDir = new File(PATH_TO_FO76_DUMP, "international");
    return new File(internationalDir, signature + ".json");
  }

  private static String getId(Object input) {
    if (Objects.isNull(input)) {
      return null;
    }
    if (input instanceof Map) {
      return null;
    }
    String id = input.toString().replace("<Error: No strings file for lstring ID ", "").replace(">", "");
    return "s" + id;
  }

  private static List<LegendaryModDescriptor> getMods() throws IOException {
    return OM.readValue(new File(LEG_MODS_FILE), LEG_MOD_TYPE_REF);
  }

  private static FilterFlag getItemType(LegendaryModDescriptor legMod) {
    if (StringUtils.containsIgnoreCase(legMod.getId(), "mod_Legendary_Armor")) {
      return FilterFlag.ARMOR;
    } else if (StringUtils.containsIgnoreCase(legMod.getId(), "mod_Legendary_Weapon")) {
      if (StringUtils.containsIgnoreCase(legMod.getId(), "guns")) {
        return FilterFlag.WEAPON_RANGED;
      } else if (StringUtils.containsIgnoreCase(legMod.getId(), "melee")) {
        return FilterFlag.WEAPON_MELEE;
      } else {
        return FilterFlag.WEAPON;
      }
    }
    return FilterFlag.UNKNOWN;
  }

  private static void updateLegMod(LegendaryModDescriptor legMod, Map<String, Object> dumpEntry, boolean newEntry) {
    String descId = getId(dumpEntry.get("DESC"));
    String fullId = getId(dumpEntry.get("FULL"));
    String id = legMod.getId();

    Map<String, String> descriptions = translationsMap.get(descId);
    Map<String, String> fulls = translationsMap.get(fullId);
    String edid = String.valueOf(dumpEntry.get("EDID"));

    if (newEntry) {
      legMod.setId(edid);
      legMod.setItemType(getItemType(legMod));
      legMod.setEnabled(true);
    }
    try {
      int star = Integer.parseInt(edid.replaceAll("\\D+", ""));
      legMod.setStar(star);
    } catch (Exception e) {
      legMod.setStar(999);
    }
    if (legMod.getStar() >= 1 && legMod.getStar() <= 3 && StringUtils.isBlank(legMod.getAbbreviation())) {
      System.out.println("Missing abbreviation for: " + legMod.getGameId());
      legMod.setAbbreviation("TODO");
    }

    if (MapUtils.isNotEmpty(fulls)) {
      legMod.setTexts(fulls);
    } else {
//      System.out.println("Missing texts for: " + id);
    }
    if (MapUtils.isNotEmpty(descriptions)) {
      legMod.setTranslations(descriptions);
    } else {
//      System.out.println("Missing descriptions for: " + id);
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void updateLegModConfigsFromFo76Dump() throws Exception {
    String signature = "OMOD";
    File file = buildJsonFile(signature);
    Map<String, Object> map = OM.readValue(file, HUD_EDITOR_CONFIG);
    Map<String, Map<String, Object>> omods = (Map<String, Map<String, Object>>) map.get(signature);
    List<LegendaryModDescriptor> mods = getMods();
    for (LegendaryModDescriptor mod : mods) {
      Map<String, Object> modMap = omods.get(mod.getGameId());
      if (MapUtils.isEmpty(modMap)) {
        System.out.println("Missing entry for " + mod.getGameId());
        continue;
      }
      updateLegMod(mod, modMap, false);
    }
    OM.writeValue(new File("src/main/resources/configs/legendaryMods.config2.json"), mods);
  }
}
