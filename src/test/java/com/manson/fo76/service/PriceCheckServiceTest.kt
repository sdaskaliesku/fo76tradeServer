package com.manson.fo76.service

import com.manson.fo76.config.AppConfig
import com.manson.fo76.domain.pricing.PriceCheckResponse
import java.time.LocalDateTime
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Invocation
import javax.ws.rs.client.WebTarget
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider
import org.junit.jupiter.api.Test

internal class PriceCheckServiceTest {
    @Test
    internal fun testPriceCheck() {
        var dateStr: String = "2020-09-13 00:27:47".replace(" ", "T")
//        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        var date:Date = Date()
        val now = LocalDateTime.now()
        val data = LocalDateTime.parse(dateStr).minusHours(24)
        println(now.isAfter(data))

//        val request = PriceCheckRequest()
//        request.name = "metal"
//        request.abbreviation = "b"
//        request.armorGrade = ArmorGrade.Unknown
//        val service = PriceCheckService()
////
//        println(service.priceCheck(request))
    }

    @Test
    internal fun name() {
//        val config = ClientConfig()
//        config.register(JacksonJsonProvider(AppConfig.objectMapper))
//        val client = ClientBuilder.newClient(config)
//
//        val map: HashMap<String, Any> = HashMap()
//        map["operationName"] = "gameTaxonomyNodes"
//        val variables: HashMap<String, Any> = HashMap()
//        variables["branches"] = listOf("Effect.Armor", "Effect.Melee", "Effect.Ranged", "Effect.Weapon")
//        map["variables"] = variables
//        val webResource: WebTarget = client.target("https://a.roguetrader.com/graphql")
//        val invoke = webResource.request().accept("application/json").post(map, HashMap::class.java).invoke()
//        println(invoke)
    }
}
