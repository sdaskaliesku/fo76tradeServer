package com.manson.fo76.repository;

import com.manson.fo76.domain.fed76.PriceCheckCacheItem;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BasePriceCheckRepository extends MongoRepository<PriceCheckCacheItem, String> {

  List<PriceCheckCacheItem> findByRequestId(String requestId);
}
