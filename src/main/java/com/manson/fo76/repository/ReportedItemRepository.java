package com.manson.fo76.repository;

import com.manson.fo76.domain.ReportedItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportedItemRepository extends MongoRepository<ReportedItem, String> {

}
