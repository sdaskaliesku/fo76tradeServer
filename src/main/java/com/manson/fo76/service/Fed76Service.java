package com.manson.fo76.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.domain.config.ArmorConfig;
import com.manson.domain.fed76.BasePriceCheckResponse;
import com.manson.domain.fed76.ItemPriceCheckResponse;
import com.manson.domain.fed76.PlanPriceCheckResponse;
import com.manson.domain.fed76.PriceCheckRequest;
import com.manson.domain.fed76.mapping.MappingResponse;
import com.manson.domain.fed76.pricing.PriceEnhanceRequest;
import com.manson.domain.fed76.pricing.VendorData;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.itemextractor.ItemConfig;
import com.manson.domain.itemextractor.ItemDetails;
import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.LegendaryMod;
import com.manson.fo76.domain.fed76.PriceCheckCacheItem;
import com.manson.fo76.repository.BasePriceCheckRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Fed76Service extends BaseRestClient {

  private static final Map<ArmorGrade, String> GRADE_TO_GAME_ID_MAP = new EnumMap<>(ArmorGrade.class);
  public static boolean USE_ID = false;

  static {
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Light, "00182E78");
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Sturdy, "00182E79");
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Heavy, "00182E7A");
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Unknown, "");
  }

  private BasePriceCheckRepository priceCheckRepository;

  public Fed76Service(@Autowired ObjectMapper objectMapper) {
    super(objectMapper);
  }

  private static boolean isResponseExpired(BasePriceCheckResponse response) {
    try {
      LocalDateTime respDate = LocalDateTime.parse(response.getTimestamp().replace(" ", "T")).plusHours(24);
      return respDate.isBefore(LocalDateTime.now());
    } catch (Exception e) {
      log.error("Error parsing date {}", response);
    }
    return true;
  }

  @Autowired
  @Qualifier("priceCheckRepository")
  public void setPriceCheckRepository(BasePriceCheckRepository priceCheckRepository) {
    this.priceCheckRepository = priceCheckRepository;
  }

  private BasePriceCheckResponse performRequest(PriceCheckRequest request) {
    if (!request.isValid()) {
      return new BasePriceCheckResponse();
    }
    WebTarget webTarget;
    if (request.getFilterFlag() == FilterFlag.NOTES) {
      webTarget = planPriceCheck(request.getItem());
    } else {
      if (USE_ID) {
        webTarget = armorWeaponPriceCheck(request.getIds());
      } else {
        webTarget = armorWeaponPriceCheck(request.getItem(), request.getMods(), request.getGrade().getValue());
      }
    }
    try {
      return webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get(BasePriceCheckResponse.class);
    } catch (Exception e) {
      log.error("Error requesting price check {}\r\n{}", webTarget.getUri(), request);
    }
    return new BasePriceCheckResponse();
  }

  public final MappingResponse getMapping() {
    WebTarget webResource = this.client.target(Endpoints.MAPPING);
    return webResource.request().accept(MediaType.APPLICATION_JSON_TYPE).get(MappingResponse.class);
  }

  public final BasePriceCheckResponse priceCheck(PriceCheckRequest request) {
    BasePriceCheckResponse response;
    if (request.getFilterFlag() == FilterFlag.NOTES) {
      response = new PlanPriceCheckResponse();
    } else {
      response = new ItemPriceCheckResponse();
    }
    if (!request.isValid()) {
      return response;
    }
    List<PriceCheckCacheItem> requests = priceCheckRepository.findByRequestId(request.toId());
    if (CollectionUtils.isEmpty(requests)) {
      response = performRequest(request);
      if (StringUtils.equalsIgnoreCase(response.getDescription(), "Error\n")) {
        response.setPrice(-1);
        log.error("Error price check {} / {}", request, response);
      } else {
        saveToCache(request, response);
      }
    } else {
      log.debug("Found price check request in cache: {}", request);
      for (PriceCheckCacheItem cacheItem : requests) {
        if (isResponseExpired(cacheItem.getResponse())) {
          priceCheckRepository.delete(cacheItem);
          continue;
        }
        return cacheItem.getResponse();
      }
    }
    return response;
  }

  private void saveToCache(PriceCheckRequest request, BasePriceCheckResponse response) {
    PriceCheckCacheItem cachedResponse = new PriceCheckCacheItem();
    cachedResponse.setRequestId(request.toId());
    cachedResponse.setResponse(response);
    priceCheckRepository.save(cachedResponse);
  }

  public PriceEnhanceRequest createPriceEnhanceRequest(ItemResponse itemResponse) {
    if (itemResponse == null || itemResponse.getItemDetails() == null || itemResponse.getVendingData() == null
        || itemResponse.getVendingData().getPrice() == null || itemResponse.getVendingData().getPrice() <= 0) {
      return null;
    }
    ItemDetails itemDetails = itemResponse.getItemDetails();
    PriceEnhanceRequest request = new PriceEnhanceRequest();
    request.setVendingData(
        VendorData
            .builder()
            .price(itemResponse.getVendingData().getPrice())
            .build()
    );
    ArmorConfig config = itemDetails.getArmorConfig();
    if (Objects.nonNull(config) && config.getArmorGrade() != ArmorGrade.Unknown) {
      request.setArmorConfig(com.manson.domain.fed76.pricing.ArmorConfig.builder()
          .armorGrade(config.getArmorGrade().getValue())
          .armorId(config.getArmorId())
          .build());
    } else {
      request.setArmorConfig(null);
    }
    if (CollectionUtils.isNotEmpty(itemDetails.getLegendaryMods())) {
      List<com.manson.domain.fed76.pricing.LegendaryMod> mods = itemDetails.getLegendaryMods()
          .stream().map(x -> {
            com.manson.domain.fed76.pricing.LegendaryMod legendaryMod = new com.manson.domain.fed76.pricing.LegendaryMod();
            legendaryMod.setAbbreviation(x.getAbbreviation());
            legendaryMod.setGameId(x.getGameId());
            legendaryMod.setText(x.getText());
            return legendaryMod;
          }).collect(Collectors.toList());
      request.setLegendaryMods(mods);
    }
    if (request.isValid()) {
      return request;
    }
    return null;
  }

  public Response enhancePriceCheck(PriceEnhanceRequest request) {
    return client
        .target(Endpoints.ENHANCE_PRICE_API)
        .request()
        .accept(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.json(request));
  }

  public PriceCheckRequest createPriceCheckRequest(ItemResponse itemResponse) {
    PriceCheckRequest request = new PriceCheckRequest();
    ItemDetails itemDetails = itemResponse.getItemDetails();
    ItemConfig configName = itemDetails.getConfig();
    if (Objects.isNull(configName)) {
      return request;
    }
    request.setFilterFlag(configName.getType());
    List<String> ids = new ArrayList<>();
    ids.add(configName.getGameId());
    if (CollectionUtils.isNotEmpty(itemDetails.getLegendaryMods())) {
      List<String> modIds = itemDetails.getLegendaryMods().stream().map(LegendaryMod::getGameId)
          .collect(Collectors.toList());
      ids.addAll(modIds);
      request.setMods(String.join("/", modIds));
    }
    ArmorConfig armorConfig = itemDetails.getArmorConfig();
    if (armorConfig != null) {
      request.setGrade(armorConfig.getArmorGrade());
      String gradeId = GRADE_TO_GAME_ID_MAP.get(armorConfig.getArmorGrade());
      if (StringUtils.isNotBlank(gradeId)) {
        request.setGradeId(gradeId);
        ids.add(gradeId);
      }
    }
    request.setIds(ids);
    request.setItem(configName.getGameId());
    return request;
  }

  private WebTarget armorWeaponPriceCheck(String item, String mods, String grade) {
    WebTarget webTarget = client
        .target(Endpoints.ARMOR_WEAPON_PRICE_API)
        .queryParam("item", item)
        .queryParam("mods", mods);
    if (StringUtils.isNotBlank(grade) && !StringUtils.equalsIgnoreCase(ArmorGrade.Unknown.getValue(), grade)) {
      webTarget = webTarget.queryParam("grade", grade);
    }
    return webTarget;
  }

  private WebTarget armorWeaponPriceCheck(List<String> ids) {
    WebTarget webTarget = client
        .target(Endpoints.ARMOR_WEAPON_PRICE_API);
    String idsParam = String.join("-", ids);
    webTarget = webTarget.queryParam("ids", idsParam);
    return webTarget;
  }

  private WebTarget planPriceCheck(String item) {
    return client
        .target(Endpoints.PLAN_PRICE_API)
        .queryParam("id", item);
  }

  private static class Endpoints {

    private static final String BASE = "https://fed76.info/";
    public static final String MAPPING = BASE + "pricing/mapping";
    public static final String PLAN_PRICE_API = BASE + "plan-api/";
    public static final String ARMOR_WEAPON_PRICE_API = BASE + "pricing-api/";
    public static final String ENHANCE_PRICE_API = BASE + BASE + "pricing/parse";
  }
}
