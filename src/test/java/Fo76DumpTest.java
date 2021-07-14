import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.domain.config.Fed76Config;
import com.manson.fo76.service.Fed76Service;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class Fo76DumpTest {

  private static final List<String> SIGNATURES = Arrays
      .asList("ARMO", "WEAP", "AMMO", "OMOD", "ENCH", "MGEF", "COBJ", "NOTE", "BOOK", "MISC", "CONT", "FLOR", "NPC_",
          "RACE", "MSWP", "STAT", "KYWD", "LVLI", "UTIL");
  private static SessionFactory sessionFactory;
  private static final ObjectMapper OM = new ObjectMapper();
  private static final String PATH_TO_FO76_DUMP = "C:\\fo76tools\\jdump\\c82fbd7b9b21b405a4bf92d123fe4c04c402d90c";

  @BeforeAll
  public static void beforeAll() {
//    OM.setSerializationInclusion(Include.ALWAYS);
    // disable logging
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    loggerContext.stop();
    Configuration configuration = new Configuration();
    configuration.configure();
    sessionFactory = configuration.buildSessionFactory();
  }

  private static File buildJsonFile(String signature) {
    File internationalDir = new File(PATH_TO_FO76_DUMP, "international");
    return new File(internationalDir, signature + ".json");
  }

  @Test
  public void test() throws IOException {
    for (String signature : SIGNATURES) {
      File file = buildJsonFile(signature);
      JsonNode jsonNode = OM.readTree(file);

    }
  }
}
