package com.manson.fo76.web.api;

import com.manson.fo76.Utils;
import com.manson.fo76.domain.ModDataRequest;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.service.ItemService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemsController {

  private final ItemService itemService;

  @Autowired
  public ItemsController(ItemService itemService) {
    this.itemService = itemService;
  }

  private static PageRequest createPageRequest(Integer page, Integer size) {
    PageRequest pageRequest = null;
    if (page != null && size != null) {
      pageRequest = PageRequest.of(page, size);
    }
    return pageRequest;
  }

  @GetMapping("/findAll")
  public Page<ItemDTO> findAll(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return itemService.findAll(createPageRequest(page, size));
  }

  @GetMapping("/findAllByOwnerId")
  public Page<ItemDTO> findAllByOwnerId(@RequestParam String ownerId, @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return itemService.findAllByOwnerId(ownerId, createPageRequest(page, size));
  }

  @GetMapping("/findAllByOwnerName")
  public Page<ItemDTO> findAllByOwnerName(@RequestParam String ownerName, @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return itemService.findAllByOwnerName(ownerName, createPageRequest(page, size));
  }

  @GetMapping("/findByIdAndOwnerId")
  public ItemDTO findByIdAndOwnerId(@RequestParam String id, @RequestParam String ownerId) {
    return itemService.findByIdAndOwnerId(id, ownerId);
  }

  @GetMapping("/findByIdAndOwnerName")
  public ItemDTO findByIdAndOwnerName(@RequestParam String id, @RequestParam String ownerName) {
    return itemService.findByIdAndOwnerName(id, ownerName);
  }

  @PostMapping(
      value = "/prepareModData", consumes = "application/json", produces = "application/json")
  public List<ItemDTO> prepareModData(@RequestBody ModDataRequest modDataRequest) {
    try {
      Pair<User, List<ItemDescriptor>> pair = itemService
          .processModDataItems(modDataRequest.getModData(), modDataRequest.getFilters());
      if (pair == null) {
        return new ArrayList<>();
      }
      List<ItemDescriptor> itemDescriptors = pair.getValue();
      return Utils.convertItems(itemDescriptors, pair.getKey());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

}
