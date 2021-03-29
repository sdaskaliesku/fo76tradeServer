package com.manson.fo76.service;

import com.manson.domain.config.ArmorConfig;
import com.manson.domain.fed76.PriceCheckRequest;
import com.manson.domain.fed76.pricing.LegendaryMod;
import com.manson.domain.fed76.pricing.VendorData;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import com.manson.domain.itemextractor.ItemConfig;
import com.manson.domain.itemextractor.ItemDetails;
import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.LegendaryModConfig;
import com.manson.fo76.domain.fed76.PriceEnhanceRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class PriceRequestBuilder {

    private final ItemResponse itemResponse;

    public PriceRequestBuilder(ItemResponse itemResponse) {
        this.itemResponse = itemResponse;
    }

    public PriceEnhanceRequest createPriceEnhanceRequest() {
        if (itemResponse == null || itemResponse.getItemDetails() == null || itemResponse.getVendingData() == null
            || itemResponse.getVendingData().getPrice() == null || itemResponse.getVendingData().getPrice() <= 0) {
            return null;
        }
        ItemDetails itemDetails = itemResponse.getItemDetails();
        PriceEnhanceRequest request = new PriceEnhanceRequest();
        request.setItemName(itemDetails.getName());
        request.setGameId(itemDetails.getConfig().getGameId());
        request.setVendingData(
            VendorData
                .builder()
                .price(itemResponse.getVendingData().getPrice())
                .build()
        );
        ArmorConfig config = itemDetails.getArmorConfig();
        if (Objects.nonNull(config) && config.getArmorGrade() != ArmorGrade.Unknown) {
            request.setArmorConfig(config);
        }
        request.setLegendary(itemResponse.getIsLegendary());
        LegendaryModConfig modConfig = itemDetails.getLegendaryModConfig();
        if (CollectionUtils.isNotEmpty(modConfig.getLegendaryMods())) {
            List<LegendaryMod> mods = modConfig.getLegendaryMods()
                .stream().map(x -> {
                    com.manson.domain.fed76.pricing.LegendaryMod legendaryMod =
                        new com.manson.domain.fed76.pricing.LegendaryMod();
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

    public PriceCheckRequest createPriceCheckRequest() {
        PriceCheckRequest request = new PriceCheckRequest();
        ItemDetails itemDetails = itemResponse.getItemDetails();
        ItemConfig configName = itemDetails.getConfig();
        if (Objects.isNull(configName)) {
            return request;
        }
        request.setFilterFlag(configName.getType());
        List<String> ids = new ArrayList<>();
        ids.add(configName.getGameId());
        LegendaryModConfig modConfig = itemDetails.getLegendaryModConfig();
        if (CollectionUtils.isNotEmpty(modConfig.getLegendaryMods())) {
            List<String> modIds =
                modConfig.getLegendaryMods().stream().map(com.manson.domain.itemextractor.LegendaryMod::getGameId)
                    .collect(Collectors.toList());
            ids.addAll(modIds);
            request.setMods(String.join("/", modIds));
        }
        ArmorConfig armorConfig = itemDetails.getArmorConfig();
        if (armorConfig != null && armorConfig.getArmorGrade() != ArmorGrade.Unknown && StringUtils
            .isNotBlank(armorConfig.getGradeId())) {
            request.setGrade(armorConfig.getArmorGrade());
            request.setGradeId(armorConfig.getGradeId());
            ids.add(armorConfig.getGradeId());
        }
        request.setIds(ids);
        request.setItem(configName.getGameId());
        return request;
    }
}
