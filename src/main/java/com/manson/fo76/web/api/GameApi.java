package com.manson.fo76.web.api;

import com.manson.domain.config.ArmorConfig;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.config.XTranslatorConfig;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.fo76.items.enums.ItemCardText;
import com.manson.fo76.domain.dto.ItemResponse;
import com.manson.fo76.domain.fed76.BasePriceCheckResponse;
import com.manson.fo76.domain.fed76.PriceCheckRequest;
import com.manson.fo76.service.Fed76Service;
import com.manson.fo76.service.GameConfigHolderService;
import com.manson.fo76.service.GameConfigService;
import com.manson.fo76.service.ItemConverterService;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/game")
public class GameApi {

  private final GameConfigHolderService holderService;
  private final GameConfigService gameConfigService;
  private final Fed76Service fed76Service;

  @Autowired
  public GameApi(GameConfigHolderService holderService, GameConfigService gameConfigService,
      Fed76Service fed76Service) {
    this.holderService = holderService;
    this.gameConfigService = gameConfigService;
    this.fed76Service = fed76Service;
  }

  @GetMapping(
      produces = {"application/json"},
      value = {"/legendaryMods"}
  )
  public final List<LegendaryModDescriptor> getLegendaryModsConfig() {
    return holderService.getLegModsConfig();
  }

  @GetMapping(
      produces = {"application/json"},
      value = {"/legendaryMod"}
  )
  public final LegendaryModDescriptor getLegendaryModConfig(@RequestParam String text) {
    return this.gameConfigService.findLegendaryModDescriptor(text, FilterFlag.UNKNOWN);
  }

  @GetMapping(
      produces = {"application/json"},
      value = {"/itemCardText"}
  )
  public final ItemCardText findItemCardText(@RequestParam String text) {
    return this.gameConfigService.findItemCardText(text);
  }

  @GetMapping(
      produces = {"application/json"},
      value = {"/ammoTypes"}
  )
  public final List<XTranslatorConfig> getAmmoTypes() {
    return holderService.getAmmoTypes();
  }

  @GetMapping(
      produces = {"application/json"},
      value = {"/armorTypes"}
  )
  public final List<ArmorConfig> getArmorTypes() {
    return holderService.getArmorConfigs();
  }

  @GetMapping(
      produces = {"application/json"},
      value = {"/armorType"}
  )
  public final ArmorConfig getArmorType(@RequestParam int dr, @RequestParam int er, @RequestParam int rr) {
    return gameConfigService.findArmorType(dr, rr, er);
  }

  @PostMapping(
      produces = {"application/json"},
      consumes = {"application/json"},
      value = {"/priceCheck"}
  )
  public final BasePriceCheckResponse priceCheck(@RequestBody ItemResponse item) {
    Boolean isTradable = item.getIsTradable();
    boolean isValidFlag = ItemConverterService.SUPPORTED_PRICE_CHECK_ITEMS.contains(item.getFilterFlag());
    boolean isLegendary = item.getIsLegendary() || item.getFilterFlag() == FilterFlag.NOTES;
    boolean isInvalid = !isLegendary || !isValidFlag || !isTradable;
    if (isInvalid) {
      return new BasePriceCheckResponse();
    }
    PriceCheckRequest request = fed76Service.createPriceCheckRequest(item);
    if (Objects.isNull(request)) {
      log.error("Request is invalid, most likely invalid item {}", item.getText());
      return new BasePriceCheckResponse();
    }
    if (request.isValid()) {
      return fed76Service.priceCheck(request);
    } else {
      log.error("Request is invalid, ignoring: {} \r\n {}", request, item);
    }
    return new BasePriceCheckResponse();
  }

}
