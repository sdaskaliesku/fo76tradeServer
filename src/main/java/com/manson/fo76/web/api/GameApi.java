package com.manson.fo76.web.api;

import com.manson.domain.config.ArmorConfig;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.config.XTranslatorConfig;
import com.manson.domain.fed76.PriceCheckRequest;
import com.manson.domain.fed76.PriceEstimate;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.fo76.items.enums.ItemCardText;
import com.manson.domain.itemextractor.ItemResponse;
import com.manson.fo76.service.AppInfo;
import com.manson.fo76.service.Fed76Service;
import com.manson.fo76.service.GameConfigHolderService;
import com.manson.fo76.service.GameConfigService;
import com.manson.fo76.service.ItemConverterService;
import com.manson.fo76.service.PriceRequestBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
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

  private static final List<FilterFlagResponse> FILTER_FLAGS = Arrays
      .stream(FilterFlag.values())
      .map(FilterFlagResponse::new)
      .collect(Collectors.toList());
  private final GameConfigHolderService holderService;
  private final GameConfigService gameConfigService;
  private final Fed76Service fed76Service;

  @Autowired
  private AppInfo appInfo;

  @Autowired
  public GameApi(GameConfigHolderService holderService, GameConfigService gameConfigService,
      Fed76Service fed76Service) {
    this.holderService = holderService;
    this.gameConfigService = gameConfigService;
    this.fed76Service = fed76Service;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/legendaryMods")
  public final List<LegendaryModDescriptor> getLegendaryModsConfig() {
    return holderService.getLegModsConfig();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/legendaryMod")
  public final LegendaryModDescriptor getLegendaryModConfig(@RequestParam String text,
      @RequestParam String filterFlag) {
    return this.gameConfigService.findLegendaryModDescriptor(text, FilterFlag.fromString(filterFlag));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/itemCardText")
  public final ItemCardText findItemCardText(@RequestParam String text) {
    return this.gameConfigService.findItemCardText(text);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/ammoTypes")
  public final List<XTranslatorConfig> getAmmoTypes() {
    return holderService.getAmmoTypes();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/armorTypes")
  public final List<ArmorConfig> getArmorTypes() {
    return holderService.getArmorConfigs();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/armorType")
  public final ArmorConfig getArmorType(@RequestParam int dr, @RequestParam int er, @RequestParam int rr) {
    return gameConfigService.findArmorType(dr, rr, er);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, value = "/priceCheck")
  public final PriceEstimate priceCheck(@RequestBody ItemResponse item) {
    if (item == null) {
      return new PriceEstimate();
    }
    Boolean isTradable = item.getIsTradable();
    boolean isValidFlag = ItemConverterService.SUPPORTED_PRICE_CHECK_ITEMS.contains(item.getFilterFlag());
    boolean isLegendary = item.getIsLegendary() || item.getFilterFlag() == FilterFlag.NOTES;
    boolean isInvalid = !isLegendary || !isValidFlag || !isTradable;
    if (isInvalid) {
      return new PriceEstimate();
    }
    PriceCheckRequest request = new PriceRequestBuilder(item).createPriceCheckRequest();
    if (Objects.isNull(request)) {
      log.error("Request is invalid, most likely invalid item {}", item.getText());
      return new PriceEstimate();
    }
    if (request.isValid()) {
      return fed76Service.priceCheck(request);
    }
    log.error("Request is invalid, ignoring: {} \r\n {}", request, item);
    return new PriceEstimate();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/filterFlags")
  public List<FilterFlagResponse> filterFlags() {
    return FILTER_FLAGS;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON, value = "/appInfo")
  public AppInfo appInfo() {
    return appInfo;
  }

}
