package com.manson.fo76.service;

import static com.manson.fo76.helper.Utils.silentParse;

import com.google.common.collect.ImmutableMap;
import com.manson.domain.fed76.PriceCheckRequest;
import com.manson.domain.fed76.PriceEstimate;
import com.manson.domain.fed76.PriceType;
import com.manson.domain.fed76.response.BasePriceCheckResponse;
import com.manson.domain.fed76.response.ItemPriceCheckResponse;
import com.manson.domain.fed76.response.ItemReview;
import com.manson.domain.fed76.response.PlanPriceCheckResponse;
import com.manson.domain.fed76.response.PlanReview;
import com.manson.domain.fed76.response.PlanSubReview;
import com.manson.domain.fed76.response.PriceDetails;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.fo76.domain.fed76.PriceCheckCacheItem;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PriceCheckAdapter {

    private static final List<String> ERROR_RESPONSES = Arrays.asList(
        "Error\n",
        "Input Error",
        "Nothing matches your query",
        "Failed to interpret the key",
        "Impossible legendary effects configuration"
    );
    private static final Map<FilterFlag, PriceType> MAP = ImmutableMap.<FilterFlag, PriceType>builder()
        .put(FilterFlag.WEAPON, PriceType.ITEM)
        .put(FilterFlag.WEAPON_MELEE, PriceType.ITEM)
        .put(FilterFlag.WEAPON_RANGED, PriceType.ITEM)
        .put(FilterFlag.ARMOR, PriceType.ITEM)
        .put(FilterFlag.NOTES, PriceType.PLAN)
        .build();

    private final BasePriceCheckResponse response;
    private final FilterFlag filterFlag;

    public PriceCheckAdapter(BasePriceCheckResponse response, FilterFlag filterFlag) {
        this.response = response;
        this.filterFlag = filterFlag;
    }

    public PriceCheckCacheItem toCacheItem(PriceCheckRequest request) {
        PriceCheckCacheItem cacheItem = new PriceCheckCacheItem();
        cacheItem.setRequestId(request.toId());
        cacheItem.setResponse(response);
        return cacheItem;
    }

    public PriceEstimate toPriceEstimate() {
        return toPriceEstimate(getErrors());
    }

    public PriceEstimate toPriceEstimate(List<String> errors) {
        PriceEstimate priceEstimate = new PriceEstimate();
        priceEstimate.setPath(response.getPath());
        priceEstimate.setName(response.getName());
        priceEstimate.setTimestamp(response.getTimestamp());
        priceEstimate.setPrice(response.getPrice());
        String errorMessage = String.join(", ", errors);
        PriceType priceType = MAP.get(filterFlag);
        if (PriceType.ITEM == priceType) {
            ItemPriceCheckResponse itemResponse = (ItemPriceCheckResponse) response;
            priceEstimate.setDescription(itemResponse.getDescription());
            ItemReview review = itemResponse.getReview();
            if (Objects.isNull(review)) {
                return priceEstimate;
            }
            priceEstimate.setReviewDescription(review.getDescription());
            PriceDetails details = review.getDetails();
            if (Objects.isNull(details)) {
                return priceEstimate;
            }
            priceEstimate.setVendor(details.getVendor());
            priceEstimate.setMinPrice(silentParse(details.getMarketLow()).intValue());
            priceEstimate.setMaxPrice(silentParse(details.getMarketHigh()).intValue());
            priceEstimate.setNiche(details.getNiche());
            priceEstimate.setOriginal(silentParse(details.getOriginal()).intValue());
        } else if (PriceType.PLAN == priceType) {
            PlanPriceCheckResponse planResponse = (PlanPriceCheckResponse) response;
            priceEstimate.setDescription(planResponse.getVerdict());
            PlanReview review = planResponse.getReview();
            if (Objects.isNull(review) || Objects.isNull(review.getReview())) {
                return priceEstimate;
            }
            PlanSubReview subReview = review.getReview();
            priceEstimate.setMinPrice(subReview.getVendorMin());
            priceEstimate.setMaxPrice(subReview.getVendorMax());
        }

        if (StringUtils.isNotBlank(errorMessage)) {
            priceEstimate.setDescription(errorMessage);
            priceEstimate.setPrice(-1);
            priceEstimate.setMaxPrice(-1);
            priceEstimate.setMinPrice(-1);
            priceEstimate.setOriginal(-1);
        }
        return priceEstimate;
    }

    public List<String> getErrors() {
        List<String> errors = new ArrayList<>();
        List<String> responseErrors = new ArrayList<>();
        responseErrors.add(response.getName());
        PriceType type = response.getType();
        if (PriceType.ITEM == type) {
            ItemPriceCheckResponse itemResponse = (ItemPriceCheckResponse) response;
            responseErrors.add(itemResponse.getDescription());
            ItemReview review = itemResponse.getReview();
            if (Objects.nonNull(review)) {
                responseErrors.add(review.getDescription());
                responseErrors.add(review.getName());
            }
        } else if (PriceType.PLAN == type) {
            PlanPriceCheckResponse planResponse = (PlanPriceCheckResponse) response;
            responseErrors.add(planResponse.getMessage());
        }

        for (String error : ERROR_RESPONSES) {
            for (String respError : responseErrors) {
                if (StringUtils.containsIgnoreCase(respError, error)) {
                    errors.add(respError);
                }
            }
        }
        return errors;
    }


    public boolean isResponseExpired() {
        try {
            LocalDateTime respDate = LocalDateTime.parse(response.getTimestamp().replace(" ", "T")).plusHours(24);
            return respDate.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error parsing date {}", response);
        }
        return true;
    }
}
