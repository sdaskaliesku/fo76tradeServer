package com.manson.fo76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.manson.fo76.CharacterInventory;
import com.manson.fo76.domain.ItemsUploadFilters;
import com.manson.fo76.domain.LegendaryModDescriptor;
import com.manson.fo76.domain.ModData;
import com.manson.fo76.domain.ModDataRequest;
import com.manson.fo76.domain.ModUser;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.enums.FilterFlag;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.repository.ItemRepository;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
  private static final String LEG_MODS_CONFIG_FILE = "legendaryMods.config.json";
  private static final TypeReference<List<LegendaryModDescriptor>> LEG_MOD_TYPE_REF = new TypeReference<List<LegendaryModDescriptor>>() {
  };

  private final ItemRepository itemRepository;
  private final UserService userService;
  private final List<LegendaryModDescriptor> legendaryMods;

  @Autowired
  public ItemService(ItemRepository itemRepository, UserService userService, ObjectMapper objectMapper) {
    this.itemRepository = itemRepository;
    this.userService = userService;
    this.legendaryMods = loadMods(objectMapper);
  }

  private static void processItems(List<ItemDescriptor> items, Map<Long, ItemDescriptor> outputMap,
      ItemsUploadFilters itemsUploadFilters, String account, String character) {
    if (CollectionUtils.isEmpty(items)) {
      return;
    }
    for (ItemDescriptor itemDescriptor : items) {
      if (outputMap.containsKey(itemDescriptor.getServerHandleId())) {
        continue;
      }
      if (itemsUploadFilters.isTradableOnly()) {
        if (itemDescriptor.getTradable() == null || !itemDescriptor.getTradable()) {
          continue;
        }
      }
      FilterFlag filterFlag = itemDescriptor.getFilterFlagEnum();
      boolean matchesFilter = false;
      if (filterFlag == null) {
        LOGGER.warn("Empty filter flag for {}", itemDescriptor);
        continue;
      } else {
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
      }
      if (matchesFilter) {
        itemDescriptor.setAccountOwner(account);
        itemDescriptor.setCharacterOwner(character);
        outputMap.put(itemDescriptor.getServerHandleId(), itemDescriptor);
      }
    }
  }

  private static List<LegendaryModDescriptor> loadMods(ObjectMapper objectMapper) {
    List<LegendaryModDescriptor> legendaryMods = new ArrayList<>();
    try {
      URL resource = Resources.getResource(LEG_MODS_CONFIG_FILE);
      legendaryMods = objectMapper.readValue(resource, LEG_MOD_TYPE_REF).stream()
          .filter(LegendaryModDescriptor::isEnabled)
          .collect(
              Collectors.toList());
    } catch (Exception e) {
      LOGGER.error("Error while loading legendary mods config", e);
    }
    return legendaryMods;
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

  private Pair<ModUser, List<ItemDescriptor>> processModDataItems(ModData modData,
      ItemsUploadFilters itemsUploadFilters) {
    if (modData == null || MapUtils.isEmpty(modData.getCharacterInventories())) {
      return null;
    }
    if (modData.getUser() == null) {
      // TODO: delete this, once final UI will be ready
      ModUser user = new ModUser();
      user.setUser("temp");
      user.setPassword("temp");
      modData.setUser(user);
    }
    Collection<CharacterInventory> values = modData.getCharacterInventories().values();
    Map<Long, ItemDescriptor> allItems = new HashMap<>();
    for (CharacterInventory inventory : values) {
      List<ItemDescriptor> playerInventory = inventory.getPlayerInventory();
      List<ItemDescriptor> stashInventory = inventory.getStashInventory();
      processItems(playerInventory, allItems, itemsUploadFilters, inventory.getAccountInfoData().getName(), inventory.getCharacterInfoData().getName());
      processItems(stashInventory, allItems, itemsUploadFilters, inventory.getAccountInfoData().getName(), inventory.getCharacterInfoData().getName());
    }
    return new ImmutablePair<>(modData.getUser(), new ArrayList<>(allItems.values()));
  }

  public List<ItemDTO> prepareModData(ModDataRequest modDataRequest) {
    Pair<ModUser, List<ItemDescriptor>> pair = processModDataItems(modDataRequest.getModData(),
        modDataRequest.getFilters());
    if (pair == null) {
      return new ArrayList<>();
    }
    List<ItemDescriptor> itemDescriptors = pair.getValue();
    // todo: convert ModUser to User via userService
    ModUser modUser = pair.getKey();
    User user = new User();
    user.setPassword(modUser.getPassword());
    user.setName(modUser.getUser());
    user.setId(modUser.getId());

    return Utils.convertItems(itemDescriptors, legendaryMods, user);
  }

}
