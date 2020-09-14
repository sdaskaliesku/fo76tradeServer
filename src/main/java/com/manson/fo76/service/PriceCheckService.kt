package com.manson.fo76.service


import com.manson.fo76.config.AppConfig
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.pricing.PriceCheckCacheItem
import com.manson.fo76.domain.pricing.PriceCheckRequest
import com.manson.fo76.domain.pricing.PriceCheckResponse
import com.manson.fo76.repository.PriceCheckRepository
import java.time.LocalDateTime
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.WebTarget
import org.apache.commons.lang3.StringUtils
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PriceCheckService(@Autowired private val priceCheckRepository: PriceCheckRepository) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PriceCheckService::class.java)
    }

    private val client: Client

    init {
        val config = ClientConfig()
        config.register(JacksonJsonProvider(AppConfig.objectMapper))
        this.client = ClientBuilder.newClient(config)
    }

    private fun performRequest(request: PriceCheckRequest): PriceCheckResponse {
        val webResource: WebTarget = client
                .target("https://fed76.info/pricing-api/")
                .queryParam("item", request.name)
                .queryParam("mods", request.abbreviation)
                .queryParam("grade", request.armorGrade.value)
        return webResource.request().accept("application/json").get(PriceCheckResponse::class.java)
    }

    private fun isResponseExpired(response: PriceCheckResponse): Boolean {
        val respDate = LocalDateTime.parse(response.timestamp.replace(" ", "T")).plusHours(24)
        return respDate.isBefore(LocalDateTime.now())
    }

    fun createPriceCheckRequest(item: ItemDTO): PriceCheckRequest {
        val priceCheckRequest = PriceCheckRequest()
        priceCheckRequest.armorGrade = item.itemDetails.armorGrade
//        priceCheckRequest.abbreviation = item.legendaryMods.stream()
//                .map<String>(LegendaryMod::gameId)
//                .filter(StringUtils::isNotBlank)
//                .collect(joining("/"))
        priceCheckRequest.abbreviation = item.itemDetails.abbreviation
        priceCheckRequest.name = item.itemDetails.fedName
        return priceCheckRequest
    }

    fun priceCheck(request: PriceCheckRequest): PriceCheckResponse {
        if (!request.isValid()) {
            LOGGER.warn("Request is invalid, ignoring {}", request)
            return PriceCheckResponse()
        }
        val cachedResponse: PriceCheckCacheItem? = priceCheckRepository.findByRequestId(request.toId())
        if (cachedResponse != null) {
            LOGGER.debug("Found request in cache {}", request)
            val response = cachedResponse.response
            if (isResponseExpired(response)) {
                LOGGER.debug("Request has expired {}", request)
                priceCheckRepository.delete(cachedResponse)
            } else {
                LOGGER.debug("Price check Request is valid {}", request)
                return response
            }
        }
        LOGGER.debug("Cache miss for request {}", request)
        val response = performRequest(request)
        if (StringUtils.equalsIgnoreCase(response.description, "Error\n")) {
            LOGGER.error("Error: $request\t$response")
        } else {
            saveToCache(request, response)
        }
        return response

    }

    private fun saveToCache(request: PriceCheckRequest, response: PriceCheckResponse) {
        val cachedResponse = PriceCheckCacheItem()
        cachedResponse.requestId = request.toId()
        cachedResponse.response = response
        priceCheckRepository.save(cachedResponse)
    }
}