package com.manson.fo76.service;

import com.manson.fo76.domain.ModDataRequest;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.OwnerInfo;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.repository.ItemRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

  private final ItemRepository itemRepository;
  private final UserService userService;
  private final ItemConverterService itemConverterService;

  @Autowired
  public ItemService(ItemRepository itemRepository, UserService userService,
      ItemConverterService itemConverterService) {
    this.itemRepository = itemRepository;
    this.userService = userService;
    this.itemConverterService = itemConverterService;
  }

  public Page<ItemDTO> findAll(Pageable pageable) {
    if (pageable == null) {
      return new PageImpl<>(itemRepository.findAll());
    }
    return itemRepository.findAll(pageable);
  }

  public Page<ItemDTO> findAllByOwnerId(String ownerId, Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return new PageImpl<>(itemRepository.findAllByOwnerInfoId(ownerId));
    }
    return itemRepository.findAllByOwnerInfoId(ownerId, pageable);
  }

  public Page<ItemDTO> findAllByOwnerName(String ownerName, Pageable pageable) {
    if (Objects.isNull(pageable)) {
      return new PageImpl<>(itemRepository.findAllByOwnerInfoName(ownerName));
    }
    return itemRepository.findAllByOwnerInfoName(ownerName, pageable);
  }

  public ItemDTO findByIdAndOwnerId(String id, String ownerId) {
    return itemRepository.findByIdAndOwnerInfoId(id, ownerId);
  }

  public ItemDTO findByIdAndOwnerName(String id, String ownerName) {
    return itemRepository.findByIdAndOwnerInfoName(id, ownerName);
  }

  public boolean deleteAllByOwnerId(String ownerId) {
    itemRepository.deleteAllByOwnerInfoId(ownerId);
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
    OwnerInfo ownerInfo = itemDTO.getOwnerInfo();
    if (Objects.isNull(ownerInfo)) {
      ownerInfo = new OwnerInfo();
    }
    ownerInfo.setName(user.getName());
    ownerInfo.setId(user.getId());
    itemDTO.setOwnerInfo(ownerInfo);
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

  public List<ItemDTO> prepareModData(ModDataRequest modDataRequest) {
    return itemConverterService.prepareModData(modDataRequest);
  }

}
