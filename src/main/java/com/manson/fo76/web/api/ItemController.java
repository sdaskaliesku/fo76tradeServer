package com.manson.fo76.web.api;


import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.itemextractor.ItemConfig;
import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.ItemsUploadFilters;
import com.manson.domain.itemextractor.ModData;
import com.manson.domain.itemextractor.ModDataRequest;
import com.manson.fo76.domain.ItemContext;
import com.manson.fo76.domain.ReportedItem;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.service.ItemService;
import java.util.List;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
        @QueryParam(value = "autoPriceCheck") boolean autoPriceCheck,
        @QueryParam(value = "fed76Enhance") boolean fed76Enhance,
        @QueryParam(value = "shortenResponse") boolean shortenResponse,
        @QueryParam(value = "toCSV") boolean toCSV,
        @RequestBody ModData modData) {
        try {
            ModDataRequest request = new ModDataRequest();
            request.setFilters(new ItemsUploadFilters());
            request.setModData(modData);
            ItemContext context = new ItemContext();
            context.setPriceCheck(autoPriceCheck);
            context.setFed76Enhance(fed76Enhance);
            context.setShortenResponse(shortenResponse);
            List<ItemResponse> responses = itemService.prepareModData(request, context);
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
    public List<ItemResponse> prepareModData(@RequestBody ModDataRequest modDataRequest,
        @QueryParam("priceCheck") boolean priceCheck, @QueryParam("fed76Enhance") boolean fed76Enhance,
        @QueryParam("shortenResponse") boolean shortenResponse) {
        ItemContext context = new ItemContext();
        context.setPriceCheck(priceCheck);
        context.setFed76Enhance(fed76Enhance);
        context.setShortenResponse(shortenResponse);
        try {
            return itemService.prepareModData(modDataRequest, context);
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

  @GetMapping(value = "reported", produces = MediaType.APPLICATION_JSON)
  public List<ReportedItem> getAllReportedItems() {
        return itemService.getAllReportedItems();
  }

//  @GetMapping(value = "itemConfig", produces = MediaType.APPLICATION_JSON)
  public ItemConfig getItemConfig(@QueryParam(value = "name") String name, @QueryParam(value = "filterFlag") String filterFlag) {
    return itemService.findItemConfig(name, FilterFlag.fromString(filterFlag));
  }
}
