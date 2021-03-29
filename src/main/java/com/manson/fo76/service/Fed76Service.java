package com.manson.fo76.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.domain.fed76.PriceCheckRequest;
import com.manson.domain.fed76.PriceEstimate;
import com.manson.domain.fed76.PriceType;
import com.manson.domain.fed76.mapping.MappingResponse;
import com.manson.domain.fed76.response.BasePriceCheckResponse;
import com.manson.domain.fed76.response.ItemPriceCheckResponse;
import com.manson.domain.fed76.response.PlanPriceCheckResponse;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.fo76.domain.config.Fed76Config;
import com.manson.fo76.domain.fed76.PriceCheckCacheItem;
import com.manson.fo76.domain.fed76.PriceEnhanceRequest;
import com.manson.fo76.repository.PriceCheckRepository;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Fed76Service extends BaseRestClient {

    private final Fed76Config fed76Config;

    private PriceCheckRepository priceCheckRepository;

    public Fed76Service(@Autowired Fed76Config fed76Config, @Autowired ObjectMapper objectMapper) {
        super(objectMapper);
        this.fed76Config = fed76Config;
    }


    @Autowired
    public void setPriceCheckRepository(PriceCheckRepository priceCheckRepository) {
        this.priceCheckRepository = priceCheckRepository;
    }

    private BasePriceCheckResponse performRequest(PriceCheckRequest request) {
        if (!request.isValid()) {
            return new BasePriceCheckResponse();
        }
        PriceType type = PriceType.ITEM;
        Class<? extends BasePriceCheckResponse> respClass = ItemPriceCheckResponse.class;
        WebTarget webTarget;
        if (request.getFilterFlag() == FilterFlag.NOTES) {
            webTarget = planPriceCheck(request.getItem());
            type = PriceType.PLAN;
            respClass = PlanPriceCheckResponse.class;
        } else {
            if (fed76Config.isUseIdForPriceCheck()) {
                webTarget = armorWeaponPriceCheck(request.getIds());
            } else {
                webTarget = armorWeaponPriceCheck(request.getItem(), request.getMods(), request.getGrade().getValue());
            }
        }
        try {
            BasePriceCheckResponse response = webTarget.request().accept(MediaType.APPLICATION_JSON_TYPE).get(respClass);
            if (Objects.nonNull(response)) {
                response.setType(type);
            }
            return response;
        } catch (Exception e) {
            log.error("Error requesting price check {}\r\n{}", webTarget.getUri(), request);
        }
        return new BasePriceCheckResponse();
    }

    public final MappingResponse getMapping() {
        WebTarget webResource = this.client.target(fed76Config.getMappingUrl());
        return webResource.request().accept(MediaType.APPLICATION_JSON_TYPE).get(MappingResponse.class);
    }

    public final PriceEstimate priceCheck(PriceCheckRequest request) {
        BasePriceCheckResponse response;
        if (!request.isValid()) {
            return null;
        }
        List<PriceCheckCacheItem> requests = priceCheckRepository.findByRequestId(request.toId());
        if (CollectionUtils.isEmpty(requests)) {
            response = performRequest(request);
            PriceCheckAdapter adapter = new PriceCheckAdapter(response, request.getFilterFlag());
            List<String> errors = adapter.getErrors();
            if (CollectionUtils.isNotEmpty(errors)) {
                log.error("Error price check {} / {}", request, response);
                return adapter.toPriceEstimate(errors);
            }
            BasePriceCheckResponse priceCheckResponse = saveToCache(adapter.toCacheItem(request)).getResponse();
            return new PriceCheckAdapter(priceCheckResponse, request.getFilterFlag()).toPriceEstimate();
        } else {
            log.debug("Found price check request in cache: {}", request);
            for (PriceCheckCacheItem cacheItem : requests) {
                PriceCheckAdapter adapter = new PriceCheckAdapter(cacheItem.getResponse(), request.getFilterFlag());
                List<String> errors = adapter.getErrors();
                boolean hasErrors = CollectionUtils.isEmpty(adapter.getErrors());
                if (adapter.isResponseExpired() || hasErrors) {
                    priceCheckRepository.delete(cacheItem);
                    return priceCheck(request);
                }
                if (CollectionUtils.isEmpty(errors)) {
                    return adapter.toPriceEstimate();
                }
            }
        }
        return null;
    }

    private PriceCheckCacheItem saveToCache(PriceCheckCacheItem cacheItem) {
        return priceCheckRepository.save(cacheItem);
    }

    public Response enhancePriceCheck(PriceEnhanceRequest request) {
        return client
            .target(fed76Config.getPriceEnhanceUrl())
            .request()
            .accept(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(request));
    }

    private WebTarget armorWeaponPriceCheck(String item, String mods, String grade) {
        WebTarget webTarget = client
            .target(fed76Config.getItemPricingUrl())
            .queryParam("item", item)
            .queryParam("mods", mods);
        if (StringUtils.isNotBlank(grade) && !StringUtils.equalsIgnoreCase(ArmorGrade.Unknown.getValue(), grade)) {
            webTarget = webTarget.queryParam("grade", grade);
        }
        return webTarget;
    }

    private WebTarget armorWeaponPriceCheck(List<String> ids) {
        WebTarget webTarget = client.target(fed76Config.getItemPricingUrl());
        String idsParam = String.join("-", ids);
        webTarget = webTarget.queryParam("ids", idsParam);
        return webTarget;
    }

    private WebTarget planPriceCheck(String item) {
        return client
            .target(fed76Config.getPlanPricingUrl())
            .queryParam("id", item);
    }

}
