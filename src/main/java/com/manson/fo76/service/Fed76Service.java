package com.manson.fo76.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.domain.LegendaryMod;
import com.manson.domain.fed76.mapping.MappingResponse;
import com.manson.domain.fed76.pricing.PriceCheckResponse;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.fo76.domain.dto.ItemConfig;
import com.manson.fo76.domain.dto.ItemDetails;
import com.manson.fo76.domain.dto.ItemResponse;
import com.manson.fo76.domain.fed76.PriceCheckCacheItem;
import com.manson.fo76.domain.fed76.PriceCheckRequest;
import com.manson.fo76.repository.PriceCheckRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Fed76Service extends BaseRestClient {

  public static boolean USE_ID = true;

  private final PriceCheckRepository priceCheckRepository;

  @Autowired
  public Fed76Service(ObjectMapper objectMapper, PriceCheckRepository priceCheckRepository) {
    super(objectMapper);
    this.priceCheckRepository = priceCheckRepository;
  }

  private static boolean isResponseExpired(PriceCheckResponse response) {
    try {
      LocalDateTime respDate = LocalDateTime.parse(response.getTimestamp().replace(" ", "T")).plusHours(24);
      return respDate.isBefore(LocalDateTime.now());
    } catch (Exception e) {
      log.error("Error parsing date {}", response);
    }
    return true;
  }

  private PriceCheckResponse performRequest(PriceCheckRequest request) {
    if (!request.isValid()) {
      return new PriceCheckResponse();
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
      return webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get(PriceCheckResponse.class);
    } catch (Exception e) {
      log.error("Error requesting price check {}", request);
    }
    return new PriceCheckResponse();
  }

  public final MappingResponse getMapping() {
    WebTarget webResource = this.client.target(Endpoints.MAPPING);
    return webResource.request().accept(MediaType.APPLICATION_JSON_TYPE).get(MappingResponse.class);
  }


  public final PriceCheckResponse priceCheck(PriceCheckRequest request) {
    PriceCheckResponse response = new PriceCheckResponse();
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

  private void saveToCache(PriceCheckRequest request, PriceCheckResponse response) {
    PriceCheckCacheItem cachedResponse = new PriceCheckCacheItem();
    cachedResponse.setRequestId(request.toId());
    cachedResponse.setResponse(response);
    priceCheckRepository.save(cachedResponse);
  }

  public PriceCheckRequest createPriceCheckRequest(ItemResponse itemResponse) {
    PriceCheckRequest request = new PriceCheckRequest();
    ItemDetails itemDetails = itemResponse.getItemDetails();
    ItemConfig configName = itemDetails.getConfig();
    if (Objects.isNull(configName)) {
      return request;
    }
    request.setFilterFlag(configName.getType());
    request.setGrade(itemDetails.getArmorGrade());
    if (USE_ID) {
      List<String> ids = new ArrayList<>();
      ids.add(configName.getGameId());
      if (CollectionUtils.isNotEmpty(itemDetails.getLegendaryMods())) {
        ids.addAll(itemDetails.getLegendaryMods().stream().map(LegendaryMod::getGameId).collect(Collectors.toList()));
      }
      String armorGradeId = GRADE_TO_GAME_ID_MAP.get(itemDetails.getArmorGrade());
      if (StringUtils.isNotBlank(armorGradeId)) {
        ids.add(armorGradeId);
      }
      request.setIds(ids);
      request.setItem(configName.getGameId());
    } else {
      request.setMods(itemDetails.getAbbreviation());
      request.setItem(configName.getText());
    }
    if (request.getFilterFlag() == FilterFlag.NOTES) {
      request = new PriceCheckRequest();
      request.setFilterFlag(configName.getType());
      request.setItem(configName.getGameId());
    }
    return request;
  }

  private WebTarget armorWeaponPriceCheck(String item, String mods, String grade) {
    WebTarget webTarget = client
        .target(Endpoints.ARMOR_WEAPON_PRICE_API)
        .queryParam("item", item)
        .queryParam("mods", mods);
    if (StringUtils.isNotBlank(grade)) {
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

  private static final Map<ArmorGrade, String> GRADE_TO_GAME_ID_MAP = new EnumMap<>(ArmorGrade.class);

  static {
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Light, "00182E78");
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Sturdy, "00182E79");
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Heavy, "00182E7A");
    GRADE_TO_GAME_ID_MAP.put(ArmorGrade.Unknown, "");
  }

  private static class Endpoints {

    private static final String BASE = "https://fed76.info/";
    public static final String MAPPING = BASE + "pricing/mapping";
    public static final String PLAN_PRICE_API = BASE + "plan-api/";
    public static final String ARMOR_WEAPON_PRICE_API = BASE + "pricing-api/";
  }
}
