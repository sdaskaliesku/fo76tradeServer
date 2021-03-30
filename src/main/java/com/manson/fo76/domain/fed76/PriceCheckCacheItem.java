package com.manson.fo76.domain.fed76;

import com.manson.domain.fed76.response.BasePriceCheckResponse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class PriceCheckCacheItem {

    @Id
    private String id;
    @Indexed
    private String requestId;
    private BasePriceCheckResponse response;

}