package com.manson.fo76.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public abstract class PriceCheckRepository implements BasePriceCheckRepository {

  public PriceCheckRepository() {
    System.out.println("PriceCheckRepository!==============================================================");
  }
}
