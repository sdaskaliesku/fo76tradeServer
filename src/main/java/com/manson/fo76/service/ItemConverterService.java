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
import com.manson.fo76.domain.dto.OwnerInfo;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.enums.FilterFlag;
import com.manson.fo76.helper.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("UnstableApiUsage")
@Service
public class ItemConverterService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ItemConverterService.class);
  private static final String LEG_MODS_CONFIG_FILE = "legendaryMods.config.json";
  private static final TypeReference<List<LegendaryModDescriptor>> LEG_MOD_TYPE_REF = new TypeReference<List<LegendaryModDescriptor>>() {
  };

  private final List<LegendaryModDescriptor> legendaryMods;

  @Autowired
  public ItemConverterService(ObjectMapper objectMapper) {
    this.legendaryMods = loadMods(objectMapper);
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

  private static boolean matchesFilter(ItemsUploadFilters filter, ItemDescriptor itemDescriptor) {
    if (filter.isTradableOnly()) {
      if (itemDescriptor.getTradable() == null || !itemDescriptor.getTradable()) {
        return false;
      }
    }
    FilterFlag filterFlag = itemDescriptor.getFilterFlagEnum();
    if (filterFlag == null) {
      LOGGER.warn("Empty filter flag for {}", itemDescriptor);
      return false;
    } else {
      if (filter.isLegendaryOnly()) {
        Integer stars = itemDescriptor.getNumLegendaryStars();
        if (stars == null || stars <= 0) {
          return false;
        }
      }
      if (CollectionUtils.isEmpty(filter.getFilterFlags())) {
        return true;
      } else {
        for (Integer flag : filterFlag.getFlags()) {
          if (filter.getFilterFlags().contains(flag)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static void processItems(List<ItemDescriptor> items, List<ItemDescriptor> outputList,
      ItemsUploadFilters itemsUploadFilters, String account, String character) {
    if (CollectionUtils.isEmpty(items)) {
      return;
    }
    for (ItemDescriptor itemDescriptor : items) {
      if (matchesFilter(itemsUploadFilters, itemDescriptor)) {
        OwnerInfo ownerInfo = new OwnerInfo();
        ownerInfo.setAccountOwner(account);
        ownerInfo.setCharacterOwner(character);
        itemDescriptor.setOwnerInfo(ownerInfo);
        outputList.add(itemDescriptor);
      }
    }
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
    List<ItemDescriptor> allItems = new ArrayList<>();
    for (CharacterInventory inventory : values) {
      List<ItemDescriptor> allCharacterItems = new ArrayList<>();
      allCharacterItems.addAll(inventory.getPlayerInventory());
      allCharacterItems.addAll(inventory.getStashInventory());
      processItems(allCharacterItems, allItems, itemsUploadFilters, inventory.getAccountInfoData().getName(),
          inventory.getCharacterInfoData().getName());
    }
    return new ImmutablePair<>(modData.getUser(), allItems);
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
