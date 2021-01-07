package com.manson.fo76.repository;

import com.manson.fo76.domain.fed76.PriceCheckCacheItem;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceCheckRepository extends MongoRepository<PriceCheckCacheItem, String> {

  List<PriceCheckCacheItem> findByRequestId(String requestId);

}
