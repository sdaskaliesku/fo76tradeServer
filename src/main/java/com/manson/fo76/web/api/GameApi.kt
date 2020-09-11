package com.manson.fo76.web.api

import com.manson.fo76.domain.ArmorConfig
import com.manson.fo76.domain.LegendaryModDescriptor
import com.manson.fo76.domain.XTranslatorConfig
import com.manson.fo76.domain.items.enums.ArmorGrade
import com.manson.fo76.domain.items.enums.ItemCardText
import com.manson.fo76.service.GameConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/game")
class GameApi @Autowired constructor(private val gameConfigService: GameConfigService) {

    @GetMapping(value = ["/legendaryMods"], produces = ["application/json"])
    fun getLegendaryModsConfig(): List<LegendaryModDescriptor> {
        return gameConfigService.legModsConfig;
    }

    @GetMapping(value = ["/legendaryMod"], produces = ["application/json"])
    fun getLegendaryModConfig(@RequestParam text: String?): LegendaryModDescriptor? {
        return gameConfigService.findLegendaryModDescriptor(text)
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
    fun getCleanItemName(@RequestParam input: String): String {
        return gameConfigService.getPossibleItemName(input)
    }

    @GetMapping(value = ["/nameModifiers"], produces = ["application/json"])
    fun getNameModifiers(): List<XTranslatorConfig> {
        return gameConfigService.nameModifiers
    }
}