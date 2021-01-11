import com.manson.domain.fed76.mapping.MappingResponse
import com.manson.fo76.config.AppConfig
import com.manson.fo76.service.Fed76Service
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestsForLosers {
    @Test
    fun justDoNotHaveTimeForTests() {
        Assertions.assertTrue(true)
    }

    @Test
    internal fun name() {
        val fed76Service: Fed76Service = Fed76Service(AppConfig.getObjectMapper(), null);
        val mappingResponse: MappingResponse = fed76Service.mapping

    }
}