package com.manson.fo76.service;

import com.manson.fo76.domain.ItemsUploadFilters;
import com.manson.fo76.domain.ModData;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.enums.FilterFlag;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.repository.ItemRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final UserService userService;

  @Autowired
  public ItemService(ItemRepository itemRepository, UserService userService) {
    this.itemRepository = itemRepository;
    this.userService = userService;
  }

  private static void processItems(List<ItemDescriptor> items, Map<Long, ItemDescriptor> map,
      ItemsUploadFilters itemsUploadFilters) {
    for (ItemDescriptor itemDescriptor : items) {
      if (map.containsKey(itemDescriptor.getServerHandleId())) {
        continue;
      }
      if (itemDescriptor.getTradable() == null || !itemDescriptor.getTradable()) {
        continue;
      }
      FilterFlag filterFlag = itemDescriptor.getFilterFlagEnum();
      boolean matchesFilter = false;
      if (filterFlag == null) {
        System.out.println("Empty filter flag");
        continue;
      } else {
        if (itemsUploadFilters != null) {
          if (itemsUploadFilters.isLegendaryOnly()) {
            Integer stars = itemDescriptor.getNumLegendaryStars();
            if (stars == null || stars <= 0) {
              continue;
            }
          }
          if (CollectionUtils.isEmpty(itemsUploadFilters.getFilterFlags())) {
            matchesFilter = true;
          } else {
            for (Integer flag : filterFlag.getFlags()) {
              if (itemsUploadFilters.getFilterFlags().contains(flag)) {
                matchesFilter = true;
                break;
              }
            }
          }
        } else {
          matchesFilter = true;
        }
      }
      if (matchesFilter) {
        map.put(itemDescriptor.getServerHandleId(), itemDescriptor);
      }
    }
  }

  public Page<ItemDTO> findAll(Pageable pageable) {
    if (pageable == null) {
      return new PageImpl<>(itemRepository.findAll());
    }
    return itemRepository.findAll(pageable);
  }

  public Page<ItemDTO> findAllByOwnerId(String ownerId, Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return new PageImpl<>(itemRepository.findAllByOwnerId(ownerId));
    }
    return itemRepository.findAllByOwnerId(ownerId, pageable);
  }

  public Page<ItemDTO> findAllByOwnerName(String ownerName, Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return new PageImpl<>(itemRepository.findAllByOwnerName(ownerName));
    }
    return itemRepository.findAllByOwnerName(ownerName, pageable);
  }

  public ItemDTO findByIdAndOwnerId(String id, String ownerId) {
    return itemRepository.findByIdAndOwnerId(id, ownerId);
  }

  public ItemDTO findByIdAndOwnerName(String id, String ownerName) {
    return itemRepository.findByIdAndOwnerName(id, ownerName);
  }

  public boolean deleteAllByOwnerId(String ownerId) {
    itemRepository.deleteAllByOwnerId(ownerId);
    return true;
  }

  public void deleteItems(List<ItemDTO> items) {
    itemRepository.deleteAll(items);
  }

  public ItemDTO addItem(String userId, ItemDTO itemDTO) {
    User user = userService.findByIdOrName(userId);
    return addItem(user, itemDTO);
  }

  public ItemDTO addItem(User user, ItemDTO itemDTO) {
    if (Objects.isNull(user)) {
      return null;
    }
    User userInDb = userService.findByIdOrName(user.getId());
    if (!Utils.validatePassword(user, userInDb)) {
      return null;
    }
    itemDTO.setOwnerName(user.getName());
    itemDTO.setOwnerId(user.getId());
    return itemRepository.save(itemDTO);
  }

  public List<ItemDTO> addItems(String userId, List<ItemDTO> itemsDTO) {
    return itemsDTO.stream().map(itemDTO -> addItem(userId, itemDTO)).collect(Collectors.toList());
  }

  public List<ItemDTO> addItems(User user, List<ItemDTO> itemsDTO) {
    return itemsDTO.stream().map(itemDTO -> addItem(user, itemDTO)).collect(Collectors.toList());
  }

  public Page<ItemDTO> findAllByOwnerId(User user, Pageable pageable) {
    return findAllByOwnerId(user.getId(), pageable);
  }

  public Page<ItemDTO> findAllByOwnerName(User user, Pageable pageable) {
    return findAllByOwnerName(user.getName(), pageable);
  }

  public Pair<User, List<ItemDescriptor>> processModDataItems(ModData modData,
      ItemsUploadFilters itemsUploadFilters) {
    if (modData == null || CollectionUtils.isEmpty(modData.getInventoryList()) || CollectionUtils
        .isEmpty(modData.getPlayerInventory()) || CollectionUtils.isEmpty(modData.getStashInventory())
        || modData.getUser() == null) {
      return null;
    }
    List<ItemDescriptor> inventoryList = modData.getInventoryList();
    List<ItemDescriptor> playerInventory = modData.getPlayerInventory();
    List<ItemDescriptor> stashInventory = modData.getStashInventory();
    Map<Long, ItemDescriptor> allItems = new HashMap<>();
    processItems(inventoryList, allItems, itemsUploadFilters);
    processItems(playerInventory, allItems, itemsUploadFilters);
    processItems(stashInventory, allItems, itemsUploadFilters);
    return new ImmutablePair<>(modData.getUser(), new ArrayList<>(allItems.values()));
  }

}
