import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.underscore.lodash.U;
import com.manson.fo76.domain.LegendaryModDescriptor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PopulateLegModsConfig {

  private static final ObjectMapper OM = new ObjectMapper();
  private static final TypeReference<List<LegendaryModDescriptor>> TYPE_REFERENCE = new TypeReference<List<LegendaryModDescriptor>>() {
  };

  private static Map<String, Fo76String> parseXml(File file) throws IOException {
    String xml = String.join("\n", Files.readAllLines(Paths.get(file.toURI())));
    xml = xml.replace(String.valueOf(xml.charAt(0)), "");
    Map<String, Object> map = U.fromXml(xml);
    List<Object> strings = (List<Object>) ((Map) ((Map) map.get("SSTXMLRessources")).get("Content")).get("String");
    return genericListToFo76List(strings);
  }

  public static Map<String, Fo76String> genericListToFo76List(List<Object> list) {
    Map<String, Fo76String> map = new HashMap<>();
    try {
      TypeReference<List<Fo76String>> typeReference = new TypeReference<List<Fo76String>>() {
      };
      List<Fo76String> fo76Strings = OM.convertValue(list, typeReference);
      for (Fo76String fo76String : fo76Strings) {
        map.put(fo76String.getEdid(), fo76String);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }



//  @Test
  public void dummy() throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
    xmlMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    File baseDir = new File("./fo76strings/ru");
    File input = new File("D:\\workspace\\fo76tradeServer\\src\\main\\resources\\legendaryMods.config.json");
    String locale = "ru";
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
      descriptor.getTexts().put(locale, k.getSource());
      descriptor.getTranslations().put(locale, v.getSource());
    }

    OM.writeValue(new File("legModsconfig2.json"), descriptors);
  }
}
