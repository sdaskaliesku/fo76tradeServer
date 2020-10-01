package com.manson.fo76.web.api

import com.manson.domain.config.ArmorConfig
import com.manson.domain.config.LegendaryModDescriptor
import com.manson.domain.config.XTranslatorConfig
import com.manson.domain.fed76.pricing.PriceCheckResponse
import com.manson.domain.fo76.items.enums.ArmorGrade
import com.manson.domain.fo76.items.enums.FilterFlag
import com.manson.domain.fo76.items.enums.ItemCardText
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.service.Fed76Service
import com.manson.fo76.service.GameConfigService
import com.manson.fo76.service.ItemConverterService.Companion.SUPPORTED_PRICE_CHECK_ITEMS
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameApi @Autowired constructor(private val gameConfigService: GameConfigService, private val fed76Service: Fed76Service) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GameApi::class.java)
    }

    @GetMapping(value = ["/legendaryMods"], produces = ["application/json"])
    fun getLegendaryModsConfig(): List<LegendaryModDescriptor> {
        return gameConfigService.legModsConfig
    }

    @GetMapping(value = ["/legendaryMod"], produces = ["application/json"])
    fun getLegendaryModConfig(@RequestParam text: String?): LegendaryModDescriptor? {
        return gameConfigService.findLegendaryModDescriptor(text, FilterFlag.UNKNOWN)
    }

    @GetMapping(value = ["/itemCardText"], produces = ["application/json"])
    fun findItemCardText(@RequestParam text: String?): ItemCardText? {
        return gameConfigService.findItemCardText(text)
    }

    @GetMapping(value = ["/ammoTypes"], produces = ["application/json"])
    fun getAmmoTypes(): List<XTranslatorConfig> {
        return gameConfigService.ammoTypes
    }

    @GetMapping(value = ["/armorTypes"], produces = ["application/json"])
    fun getArmorTypes(): List<ArmorConfig> {
        return gameConfigService.armorConfigs
    }

    @GetMapping(value = ["/armorType"], produces = ["application/json"])
    fun getArmorType(@RequestParam dr: Int, @RequestParam er: Int, @RequestParam rr: Int): ArmorGrade {
        return gameConfigService.findArmorType(dr, rr, er)
    }

    @GetMapping(value = ["/cleanItemName"], produces = ["application/json"])
    fun getCleanItemName(@RequestParam input: String, @RequestParam(required = false) isArmor: Boolean = false): String {
        return gameConfigService.getPossibleItemName(input, isArmor)
    }

    @GetMapping(value = ["/nameModifiers"], produces = ["application/json"])
    fun getNameModifiers(): List<XTranslatorConfig> {
        return gameConfigService.nameModifiers
    }

    @PostMapping(value = ["/priceCheck"], produces = ["application/json"], consumes = ["application/json"])
    fun priceCheck(@RequestBody item: ItemDTO): PriceCheckResponse {
        if (!item.isLegendary || !item.isTradable || !SUPPORTED_PRICE_CHECK_ITEMS.contains(item.filterFlag)) {
            return PriceCheckResponse()
        }
        val request = fed76Service.createPriceCheckRequest(item)
        return if (request.isValid()) {
            fed76Service.priceCheck(request)
        } else {
            LOGGER.error("Request is invalid, ignoring: $request\r\n$item")
            PriceCheckResponse()
        }
    }
}