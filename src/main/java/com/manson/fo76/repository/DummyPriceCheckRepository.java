package com.manson.fo76.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;


@Profile("!cache")
@Repository
public abstract class DummyPriceCheckRepository implements BasePriceCheckRepository {

  public DummyPriceCheckRepository() {
    System.out.println("Dummy!==============================================================");
  }
}
