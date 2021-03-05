package com.manson.fo76.web.api;

import static com.manson.fo76.config.AppConfig.ENABLE_AUTO_PRICE_CHECK;

import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.ItemsUploadFilters;
import com.manson.domain.itemextractor.ModData;
import com.manson.domain.itemextractor.ModDataRequest;
import com.manson.fo76.domain.ReportedItem;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.service.ItemService;
import java.util.List;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

  private final ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PostMapping(value = "/prepareModDataRaw", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Object prepareModDataRaw(
      @QueryParam(value = "autoPriceCheck") Boolean autoPriceCheck,
      @QueryParam(value = "toCSV") boolean toCSV,
      @RequestBody ModData modData) {
    try {
      ModDataRequest request = new ModDataRequest();
      request.setFilters(new ItemsUploadFilters());
      request.setModData(modData);
      List<ItemResponse> responses = itemService.prepareModData(request, autoPriceCheck);
      if (toCSV) {
        return Utils.toCSV(responses);
      }
      return responses;
    } catch (Exception e) {
      log.error("Error while preparing mod data {}", modData, e);
      throw e;
    }
  }

  @PostMapping(value = "/prepareModData", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public List<ItemResponse> prepareModData(@RequestBody ModDataRequest modDataRequest) {
    try {
      return itemService.prepareModData(modDataRequest, ENABLE_AUTO_PRICE_CHECK);
    } catch (Exception e) {
      log.error("Error while preparing mod data {}", modDataRequest, e);
      throw e;
    }
  }

  @PostMapping(value = "/report", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public ReportedItem reportItem(@RequestBody ReportedItem item) {
    try {
      return itemService.reportItem(item);
    } catch (Exception e) {
      log.error("Error reporting item {}", item, e);
      throw e;
    }

  }
}
