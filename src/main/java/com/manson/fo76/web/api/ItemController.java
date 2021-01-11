package com.manson.fo76.web.api;

import static com.manson.fo76.config.AppConfig.ENABLE_AUTO_PRICE_CHECK;

import com.manson.fo76.domain.dto.ItemResponse;
import com.manson.fo76.domain.dto.ModData;
import com.manson.fo76.domain.dto.ModDataRequest;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.service.ItemService;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

  @RequestMapping(value = "/prepareModDataRaw", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
  public Object prepareModDataRaw(
      @QueryParam(value = "autoPriceCheck") Boolean autoPriceCheck,
      @QueryParam(value = "toCSV") Boolean toCSV,
      @RequestBody ModData modData) {
    try {
      ModDataRequest request = new ModDataRequest();
      request.setModData(modData);
      List<ItemResponse> responses = itemService.prepareModData(request, autoPriceCheck);
      if (toCSV) {
        return Utils.toCSV(responses);
      }
      return responses;
    } catch (Exception e) {
      log.error("Error while preparing mod data {}", modData, e);
    }
    return null;
  }

  @RequestMapping(value = "/prepareModData", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
  public List<ItemResponse> prepareModData(@RequestBody ModDataRequest modDataRequest) {
    try {
      return itemService.prepareModData(modDataRequest, ENABLE_AUTO_PRICE_CHECK);
    } catch (Exception e) {
      log.error("Error while preparing mod data {}", modDataRequest, e);
    }
    return Collections.emptyList();
  }
}