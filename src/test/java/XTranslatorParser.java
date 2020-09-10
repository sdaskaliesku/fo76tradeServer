import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.underscore.lodash.U;
import com.manson.fo76.domain.Fo76String;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class XTranslatorParser {

  private final ObjectMapper objectMapper;

  public XTranslatorParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<Fo76String> parse(File file) {
    try {
      String xml = String.join("\n", Files.readAllLines(Paths.get(file.toURI())));
      xml = xml.replace(String.valueOf(xml.charAt(0)), "");
      Map<String, Object> map = U.fromXml(xml);
      Map sstxmlRessources = (Map) map.get("SSTXMLRessources");
      String lang = (String) ((Map)sstxmlRessources.get("Params")).get("Dest");
      List<Object> strings = (List<Object>) ((Map) sstxmlRessources.get("Content")).get("String");
      TypeReference<List<Fo76String>> typeReference = new TypeReference<List<Fo76String>>() {
      };
      List<Fo76String> fo76Strings = objectMapper.convertValue(strings, typeReference);
      fo76Strings.forEach(str -> str.setLang(lang));
      return fo76Strings;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }
}
