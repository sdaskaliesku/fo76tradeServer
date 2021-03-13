package com.manson.fo76.domain.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Fed76Config {

  @Value("${fed76.mapping.url}")
  private String mappingUrl;
  @Value("${fed76.plan.pricing.url}")
  private String planPricingUrl;
  @Value("${fed76.item.pricing.url}")
  private String itemPricingUrl;
  @Value("${fed76.price.enhance.url}")
  private String priceEnhanceUrl;
  @Value("${fed76.price.check.use_id}")
  private boolean useIdForPriceCheck;
}
