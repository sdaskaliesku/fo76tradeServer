package com.manson.fo76.service


import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.domain.fed76.mapping.MappingResponse
import com.manson.domain.fed76.pricing.PriceCheckRequest
import com.manson.domain.fed76.pricing.PriceCheckResponse
import com.manson.domain.fo76.items.enums.ArmorGrade
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.ItemDetails
import com.manson.fo76.domain.fed76.PriceCheckCacheItem
import com.manson.fo76.repository.PriceCheckRepository
import java.time.LocalDateTime
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Fed76Service(@Autowired private val priceCheckRepository: PriceCheckRepository?, objectMapper: ObjectMapper) : BaseRestClient(objectMapper) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Fed76Service::class.java)
    }

    private fun performRequest(request: PriceCheckRequest): PriceCheckResponse {
        var webResource: WebTarget = client
                .target("https://fed76.info/pricing-api/")
                .queryParam("item", request.name)
                .queryParam("mods", request.abbreviation)
        if (request.armorGrade != ArmorGrade.Unknown) {
            webResource = webResource.queryParam("grade", request.armorGrade.value)
        }

        return webResource.request().accept(MediaType.APPLICATION_JSON_TYPE).get(PriceCheckResponse::class.java)
    }

    fun getMapping(): MappingResponse {
        val webResource: WebTarget = client.target("https://fed76.info/pricing/mapping")
        return webResource.request().accept(MediaType.APPLICATION_JSON_TYPE).get(MappingResponse::class.java)
    }

    private fun isResponseExpired(response: PriceCheckResponse): Boolean {
        val respDate = LocalDateTime.parse(response.timestamp.replace(" ", "T")).plusHours(24)
        return respDate.isBefore(LocalDateTime.now())
    }

    fun createPriceCheckRequest(item: ItemDTO): PriceCheckRequest {
        val priceCheckRequest = PriceCheckRequest()
        val itemDetails:  ItemDetails = item.itemDetails
        priceCheckRequest.armorGrade = itemDetails.armorGrade
        // FIXME: use id's for abbreviation - needs IDs for weapon
//        priceCheckRequest.abbreviation = item.legendaryMods.stream()
//                .map<String>(LegendaryMod::gameId)
//                .filter(StringUtils::isNotBlank)
//                .collect(joining("/"))
        priceCheckRequest.abbreviation = itemDetails.abbreviation
        priceCheckRequest.name = itemDetails.fedName
        if (StringUtils.isBlank(itemDetails.fedName)) {
            priceCheckRequest.name = itemDetails.name
        }
        if (StringUtils.isAllBlank(itemDetails.name, itemDetails.fedName)) {
            priceCheckRequest.name = item.text.toString()
        }
        priceCheckRequest.name = priceCheckRequest.name.replace("The ", "", true)
        return priceCheckRequest
    }

    fun priceCheck(request: PriceCheckRequest): PriceCheckResponse {
        if (!request.isValid()) {
            LOGGER.warn("Request is invalid, ignoring {}", request)
            return PriceCheckResponse()
        }
        val cachedResponse: PriceCheckCacheItem? = priceCheckRepository?.findByRequestId(request.toId())
        if (cachedResponse != null) {
            LOGGER.debug("Found request in cache {}", request)
            val response = cachedResponse.response
            if (isResponseExpired(response)) {
                LOGGER.debug("Request has expired {}", request)
                priceCheckRepository?.delete(cachedResponse)
            } else {
                LOGGER.debug("Price check Request is valid {}", request)
                return response
            }
        }
        LOGGER.debug("Cache miss for request {}", request)
        val response = performRequest(request)
        if (StringUtils.equalsIgnoreCase(response.description, "Error\n")) {
            LOGGER.error("Error: $request\t$response")
            response.price = -1
        } else {
            saveToCache(request, response)
        }
        return response

    }

    private fun saveToCache(request: PriceCheckRequest, response: PriceCheckResponse) {
        val cachedResponse = PriceCheckCacheItem()
        cachedResponse.requestId = request.toId()
        cachedResponse.response = response
        priceCheckRepository?.save(cachedResponse)
    }
}