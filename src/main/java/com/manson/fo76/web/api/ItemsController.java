package com.manson.fo76.web.api;

import com.manson.fo76.domain.ModDataRequest;
import com.manson.fo76.domain.UploadItemRequest;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.service.ItemService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/items")
public class ItemsController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ItemsController.class);

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

  @ApiIgnore
  @GetMapping(value = "/findAll", produces = "application/json")
  public Page<ItemDTO> findAll(@RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return itemService.findAll(createPageRequest(page, size));
  }

  @ApiIgnore
  @GetMapping(value = "/findAllByOwnerId", produces = "application/json")
  public Page<ItemDTO> findAllByOwnerId(@RequestParam String ownerId, @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return itemService.findAllByOwnerId(ownerId, createPageRequest(page, size));
  }

  @ApiIgnore
  @GetMapping(value = "/findAllByOwnerName", produces = "application/json")
  public Page<ItemDTO> findAllByOwnerName(@RequestParam String ownerName, @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return itemService.findAllByOwnerName(ownerName, createPageRequest(page, size));
  }

  @ApiIgnore
  @GetMapping(value = "/findByIdAndOwnerId", produces = "application/json")
  public ItemDTO findByIdAndOwnerId(@RequestParam String id, @RequestParam String ownerId) {
    return itemService.findByIdAndOwnerId(id, ownerId);
  }

  @ApiIgnore
  @GetMapping(value = "/findByIdAndOwnerName", produces = "application/json")
  public ItemDTO findByIdAndOwnerName(@RequestParam String id, @RequestParam String ownerName) {
    return itemService.findByIdAndOwnerName(id, ownerName);
  }

  @PostMapping(
      value = "/prepareModData", consumes = "application/json", produces = "application/json")
  public List<ItemDTO> prepareModData(@RequestBody ModDataRequest modDataRequest) {
    try {
      return itemService.prepareModData(modDataRequest);
    } catch (Exception e) {
      LOGGER.error("Error while preparing mod data {}", modDataRequest, e);
    }
    return new ArrayList<>();
  }

  @ApiIgnore
  @PostMapping(
      value = "/upload", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Object> prepareModData(@RequestBody UploadItemRequest uploadItemRequest) {
    if (Objects.isNull(uploadItemRequest)) {
      return ResponseEntity.noContent().build();
    }
    try {
      itemService.addItems(uploadItemRequest.getUser(), uploadItemRequest.getItems());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      LOGGER.error("Error while uploading items: {}", uploadItemRequest, e);
    }
    return ResponseEntity.noContent().build();
  }

  @ApiIgnore
  @PostMapping(
      value = "/delete", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Object> delete(@RequestBody List<ItemDTO> items) {
    if (CollectionUtils.isEmpty(items)) {
      return ResponseEntity.noContent().build();
    }
    try {
      itemService.deleteItems(items);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.noContent().build();
  }

  @ApiIgnore
  @DeleteMapping(value = "/deleteAll", produces = "application/json")
  public ResponseEntity<Object> deleteAll(@RequestParam String userId) {
    if (StringUtils.isEmpty(userId)) {
      return ResponseEntity.noContent().build();
    }
    try {
      itemService.deleteAllByOwnerId(userId);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.noContent().build();
  }

}
