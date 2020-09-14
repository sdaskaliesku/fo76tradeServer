package com.manson.fo76.domain.pricing

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

class PriceCheckCacheItem {
    @Id
    var id: String? = null

    @Indexed
    var requestId = ""
    var response: PriceCheckResponse = PriceCheckResponse()
}