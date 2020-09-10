import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.domain.Fo76String;
import com.manson.fo76.domain.LegendaryModDescriptor;
import com.manson.fo76.domain.XTranslatorConfig;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class PopulateLegModsConfig {

  private static final ObjectMapper OM = new ObjectMapper();

  private static final XTranslatorParser xTranslatorParser = new XTranslatorParser(OM);
  private static final TypeReference<List<LegendaryModDescriptor>> TYPE_REFERENCE = new TypeReference<List<LegendaryModDescriptor>>() {
  };

  private static Map<String, Fo76String> parseXml(File file) {
    List<Fo76String> strings = xTranslatorParser.parse(file);
    return genericListToFo76List(strings);
  }

  public static Map<String, Fo76String> genericListToFo76List(List<Fo76String> fo76Strings) {
    Map<String, Fo76String> map = new HashMap<>();
    try {
      map = fo76Strings.stream().collect(Collectors.toMap(Fo76String::getEdid, fo76String -> fo76String, (a, b) -> b));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

  @Test
  public void dummy2() throws IOException {
    File input = new File("./test_resources/ammo_types_ru.xml");
    File output = new File("ammo.types.json");
    File input1 = new File("D:\\workspace\\fo76tradeServer\\src\\main\\resources\\ammo.types.json");
    TypeReference<List<XTranslatorConfig>> typeReference = new TypeReference<List<XTranslatorConfig>>() {
    };
    List<XTranslatorConfig> xTranslatorConfigs = OM.readValue(input1, typeReference);
    List<Fo76String> fo76Strings = xTranslatorParser.parse(input);
    List<XTranslatorConfig> configs = fo76Strings.stream().map(XTranslatorConfig::fromFo76String)
        .collect(Collectors.toList());
    for (XTranslatorConfig config : configs) {
      for (XTranslatorConfig initial : xTranslatorConfigs) {
        XTranslatorConfig.Companion.merge(initial, config);
      }
    }

    OM.writeValue(output, xTranslatorConfigs);
  }


  @Test
  public void dummy() throws IOException {
    File baseDir = new File("./test_resources/ru");
    File input = new File("D:\\workspace\\fo76tradeServer\\src\\main\\resources\\legendaryMods.config.json");
    List<LegendaryModDescriptor> descriptors = OM.readValue(input, TYPE_REFERENCE);

    File keysFile = new File(baseDir, "keys.xml");
    File valuesFile = new File(baseDir, "values.xml");
    Map<String, Fo76String> keys = parseXml(keysFile);
    Map<String, Fo76String> values = parseXml(valuesFile);

    for (LegendaryModDescriptor descriptor : descriptors) {
      if (!keys.containsKey(descriptor.getId()) || !values.containsKey(descriptor.getId())) {
        continue;
      }
      Fo76String k = keys.get(descriptor.getId());
      Fo76String v = values.get(descriptor.getId());
      descriptor.getTexts().put(k.getLang(), k.getSource());
      descriptor.getTranslations().put(v.getLang(), v.getSource());
    }

    OM.writeValue(new File("legModsconfig2.json"), descriptors);
  }
}
