package com.manson.fo76.service;


import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.ModDataRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemService {

  private final ItemConverterService itemConverterService;

  @Autowired
  public ItemService(ItemConverterService itemConverterService) {
    this.itemConverterService = itemConverterService;
  }

  public List<ItemResponse> prepareModData(ModDataRequest request, boolean autoPriceCheck) {
    return itemConverterService.prepareModData(request, autoPriceCheck);
  }

}
