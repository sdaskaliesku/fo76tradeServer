package com.manson.fo76.service;


import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.ModDataRequest;
import com.manson.fo76.domain.ReportedItem;
import com.manson.fo76.repository.ReportedItemRepository;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ItemService {

  private final ItemConverterService itemConverterService;

  private final ReportedItemRepository reportedItemRepository;

  @Autowired
  public ItemService(ItemConverterService itemConverterService, ReportedItemRepository reportedItemRepository) {
    this.itemConverterService = itemConverterService;
    this.reportedItemRepository = reportedItemRepository;
  }

  public ReportedItem reportItem(ReportedItem item) {
    item.setDate(new Date());
    return reportedItemRepository.save(item);
  }

  public List<ItemResponse> prepareModData(ModDataRequest request, boolean autoPriceCheck) {
    return itemConverterService.prepareModData(request, autoPriceCheck);
  }

}
