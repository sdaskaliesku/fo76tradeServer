package com.manson.fo76.repository

import com.manson.fo76.domain.fed76.pricing.PriceCheckCacheItem
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PriceCheckRepository : MongoRepository<PriceCheckCacheItem?, String?> {
    fun findByRequestId(requestId: String): PriceCheckCacheItem?
}