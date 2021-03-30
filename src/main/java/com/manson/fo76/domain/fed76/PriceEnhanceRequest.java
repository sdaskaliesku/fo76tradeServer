package com.manson.fo76.domain.fed76;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.manson.domain.config.ArmorConfig;
import com.manson.domain.fed76.pricing.LegendaryMod;
import com.manson.domain.fed76.pricing.VendorData;
import com.manson.domain.fo76.items.enums.ArmorGrade;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PriceEnhanceRequest {

    @JsonInclude(Include.ALWAYS)
    private String itemName;
    @JsonInclude(Include.ALWAYS)
    private String gameId;
    @JsonInclude(Include.ALWAYS)
    private VendorData vendingData;
    @JsonInclude(Include.ALWAYS)
    private ArmorConfig armorConfig;
    @JsonInclude(Include.ALWAYS)
    private List<LegendaryMod> legendaryMods;
    @JsonInclude(Include.ALWAYS)
    private boolean isLegendary;

    @JsonIgnore
    public boolean isValid() {
        boolean validPrice = Objects.nonNull(this.vendingData) && Objects.nonNull(this.vendingData.getPrice())
            && this.vendingData.getPrice() > 0;
        boolean validMods = CollectionUtils.isNotEmpty(this.legendaryMods) && this.legendaryMods.stream()
            .allMatch(x -> StringUtils.isNotBlank(x.getGameId()));
        boolean validConfig = true;
        if (Objects.nonNull(this.armorConfig) && armorConfig.getArmorGrade() != ArmorGrade.Unknown) {
            validConfig = StringUtils.isNoneBlank(armorConfig.getArmorId());
        }

        return validConfig && validMods && validPrice;
    }
}