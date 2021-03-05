package com.manson.fo76.service;

import static com.manson.fo76.config.AppConfig.ENABLE_PRICE_ENHANCE;
import static com.manson.fo76.helper.Utils.silentParse;

import com.google.common.collect.Sets;
import com.manson.domain.config.ArmorConfig;
import com.manson.domain.config.LegendaryModDescriptor;
import com.manson.domain.fed76.BasePriceCheckResponse;
import com.manson.domain.fed76.PriceCheckRequest;
import com.manson.domain.fed76.pricing.PriceEnhanceRequest;
import com.manson.domain.fo76.ItemCardEntry;
import com.manson.domain.fo76.ItemDescriptor;
import com.manson.domain.fo76.items.enums.DamageType;
import com.manson.domain.fo76.items.enums.FilterFlag;
import com.manson.domain.fo76.items.enums.ItemCardText;
import com.manson.domain.itemextractor.CharacterInventory;
import com.manson.domain.itemextractor.ItemConfig;
import com.manson.domain.itemextractor.ItemDetails;
import com.manson.domain.itemextractor.ItemResponse;
import com.manson.domain.itemextractor.ItemSource;
import com.manson.domain.itemextractor.ItemsUploadFilters;
import com.manson.domain.itemextractor.LegendaryMod;
import com.manson.domain.itemextractor.ModData;
import com.manson.domain.itemextractor.ModDataRequest;
import com.manson.domain.itemextractor.OwnerInfo;
import com.manson.domain.itemextractor.Stats;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ItemConverterService {

  public static final Set<FilterFlag> SUPPORTED_PRICE_CHECK_ITEMS = Sets
      .newHashSet(FilterFlag.WEAPON, FilterFlag.ARMOR, FilterFlag.WEAPON_MELEE, FilterFlag.WEAPON_RANGED,
          FilterFlag.NOTES);
  private static final String PRICE_CHECK_ONLY = "Price Check Only";
  private static final String TRADABLE = "Tradable";
  private static final String LEGENDARIES = "Legendaries";
  private static final String DEFAULT_LOCALE = "en";
  private static final Set<ItemCardText> IGNORED_CARDS = Sets.newHashSet(ItemCardText.LEG_MODS, ItemCardText.DESC);
  private static final Set<FilterFlag> TYPES_FOR_NAME_CONVERT = Sets
      .newHashSet(FilterFlag.ARMOR, FilterFlag.WEAPON, FilterFlag.WEAPON_MELEE, FilterFlag.WEAPON_RANGED,
          FilterFlag.NOTES);
  private final GameConfigService gameConfigService;
  private final Fed76Service fed76Service;

  @Autowired
  public ItemConverterService(GameConfigService gameConfigService, Fed76Service fed76Service) {
    this.gameConfigService = gameConfigService;
    this.fed76Service = fed76Service;
  }

  private static boolean matchesPriceCheckOnly(ItemDescriptor itemDescriptor) {
    boolean isTradable = itemDescriptor.isTradable();
    FilterFlag filterFlag = itemDescriptor.getFilterFlagEnum();
    boolean validType = SUPPORTED_PRICE_CHECK_ITEMS.contains(filterFlag);
    if (!validType || !isTradable) {
      return false;
    }
    if (filterFlag == FilterFlag.NOTES) {
      return true;
    }
    return itemDescriptor.isLegendary();
  }

  private static boolean hasCorePriceCheckConfigs(ItemResponse itemResponse) {
    boolean isTradable = itemResponse.getIsTradable();
    FilterFlag filterFlag = itemResponse.getFilterFlag();
    boolean validType = SUPPORTED_PRICE_CHECK_ITEMS.contains(filterFlag);
    if (!validType || !isTradable) {
      return false;
    }
    boolean missingItemConfig =
        Objects.isNull(itemResponse.getItemDetails()) || Objects.isNull(itemResponse.getItemDetails().getConfig());
    if (missingItemConfig) {
      return false;
    }
    boolean missingItemId = StringUtils.isBlank(itemResponse.getItemDetails().getConfig().getGameId());
    return !missingItemId;
  }

  private static boolean isValidForPriceCheckEnhance(ItemResponse response) {
    return hasCorePriceCheckConfigs(response) && response.getIsLegendary();
  }

  private static boolean matchesPriceCheckOnly(ItemResponse itemResponse) {
    if (!hasCorePriceCheckConfigs(itemResponse)) {
      return false;
    }
    if (itemResponse.getFilterFlag() == FilterFlag.NOTES) {
      return true;
    }
    return itemResponse.getIsLegendary();
  }

  private static List<Predicate<ItemDescriptor>> buildPredicates(List<String> filters) {
    List<Predicate<ItemDescriptor>> predicates = new ArrayList<>();
    for (String filter : filters) {
      if (StringUtils.isBlank(filter)) {
        continue;
      }
      FilterFlag filterFlag = FilterFlag.fromString(filter);
      if (filterFlag != null) {
        predicates.add(x -> x.getFilterFlagEnum() == filterFlag);
      } else {
        switch (filter) {
          case PRICE_CHECK_ONLY:
            predicates = new ArrayList<>();
            predicates.add(ItemConverterService::matchesPriceCheckOnly);
            break;
          case LEGENDARIES:
            predicates.add(ItemDescriptor::isLegendary);
            break;
          case TRADABLE:
            predicates.add(ItemDescriptor::isTradable);
            break;
          default:
            log.warn("Unknown filter: " + filter);
        }
      }
    }
    return predicates;
  }

  private static boolean matchesFilter(List<Predicate<ItemDescriptor>> predicates, ItemDescriptor itemDescriptor) {
    if (CollectionUtils.isEmpty(predicates)) {
      return true;
    }
    return predicates.stream().allMatch(x -> x.test(itemDescriptor));
  }

  private static OwnerInfo createOwnerInfo(String account, String character) {
    return OwnerInfo
        .builder()
        .accountName(account)
        .characterName(character)
        .build();
  }

  private static OwnerInfo createOwnerInfo(CharacterInventory characterInventory) {
    if (Objects.isNull(characterInventory) || Objects.isNull(characterInventory.getCharacterInfoData()) || Objects
        .isNull(characterInventory.getAccountInfoData())) {
      return createOwnerInfo(null, null);
    }
    String account = characterInventory.getAccountInfoData().getName();
    String character = characterInventory.getCharacterInfoData().getName();
    return createOwnerInfo(account, character);
  }

  private static String buildLegendaryAbbreviation(List<LegendaryMod> legendaryMods,
      Function<LegendaryMod, String> function) {
    return legendaryMods
        .stream()
        .filter(Objects::nonNull)
        .map(function)
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining("/"));
  }

  private static boolean shouldConvertItemName(ItemDescriptor item) {
    return TYPES_FOR_NAME_CONVERT.contains(item.getFilterFlagEnum());
  }

  private static String getDefaultText(Map<String, String> map) {
    if (MapUtils.isEmpty(map)) {
      return StringUtils.EMPTY;
    }
    return map.get(DEFAULT_LOCALE);
  }

  private static String getItemResponseKey(ItemResponse item) {
    StringBuilder key = new StringBuilder();
    String delimiter = "-";
    key.append(item.getText()).append(delimiter);
    key.append(item.getIsLegendary()).append(delimiter);
    key.append(item.getItemLevel()).append(delimiter);
    if (item.getIsLegendary()) {
      if (item.getItemDetails() != null) {
        key.append(item.getItemDetails().getAbbreviationId()).append(delimiter);
      }
    }
    return key.toString();
  }

  private static List<ItemResponse> dedupeItems(List<ItemResponse> items) {
    Map<String, ItemResponse> dedupedItems = new HashMap<>();
    for (ItemResponse itemResponse : items) {
      String key = getItemResponseKey(itemResponse);
      if (dedupedItems.containsKey(key)) {
        ItemResponse item = dedupedItems.get(key);
        item.setCount(item.getCount() + 1);
      } else {
        dedupedItems.put(key, itemResponse);
      }
    }
    return new ArrayList<>(dedupedItems.values());
  }

  private FilterFlag clarifyFilterFlag(ItemResponse item, ItemDescriptor descriptor) {
    FilterFlag filterFlag = item.getFilterFlag();
    if (filterFlag == FilterFlag.WEAPON) {
      for (ItemCardEntry itemCardEntry : descriptor.getItemCardEntries()) {
        if (itemCardEntry.getDamageTypeEnum() == DamageType.AMMO) {
          return FilterFlag.WEAPON_RANGED;
        }
        ItemCardText cardText = gameConfigService.findItemCardText(itemCardEntry);
        if (cardText == ItemCardText.ROF && silentParse(itemCardEntry.getValue()).doubleValue() > 0) {
          return FilterFlag.WEAPON_RANGED;
        }
        if (cardText == ItemCardText.MELEE_SPEED) {
          return FilterFlag.WEAPON_MELEE;
        }
        if (cardText == ItemCardText.RNG && silentParse(itemCardEntry.getValue()).doubleValue() > 0) {
          return FilterFlag.WEAPON_THROWN;
        }
      }
    }
    if (item.getItemDetails() != null) {
      if (item.getItemDetails().getConfig() != null) {
        return item.getItemDetails().getConfig().getType();
      }
    }
    return filterFlag;
  }

  private List<ItemResponse> filterItems(List<ItemDescriptor> items, ItemsUploadFilters itemsUploadFilters,
      OwnerInfo ownerInfo, ItemSource itemSource, boolean autoPriceCheck) {
    if (CollectionUtils.isEmpty(items)) {
      return new ArrayList<>();
    }
    List<Predicate<ItemDescriptor>> predicates = buildPredicates(itemsUploadFilters.getFilterFlags());
    return items.stream()
        .filter(itemDescriptor -> matchesFilter(predicates, itemDescriptor))
        .map(x -> fromItemDescriptor(x, ownerInfo, itemSource, autoPriceCheck))
        .collect(Collectors.toList());
  }

  public List<ItemResponse> prepareModData(ModData modData, ItemsUploadFilters filters, boolean autoPriceCheck) {
    if (modData == null || MapUtils.isEmpty(modData.getCharacterInventories())) {
      return new ArrayList<>();
    }
    Collection<CharacterInventory> inventories = modData.getCharacterInventories().values();
    List<ItemResponse> allItems = new ArrayList<>();

    for (CharacterInventory inventory : inventories) {
      OwnerInfo ownerInfo = createOwnerInfo(inventory);
      allItems
          .addAll(filterItems(inventory.getPlayerInventory(), filters, ownerInfo, ItemSource.PIP_BOY, autoPriceCheck));
      allItems
          .addAll(filterItems(inventory.getStashInventory(), filters, ownerInfo, ItemSource.CONTAINER, autoPriceCheck));
    }
    return dedupeItems(allItems);
  }

  private ItemResponse fromItemDescriptor(ItemDescriptor descriptor, OwnerInfo ownerInfo, ItemSource itemSource,
      boolean autoPriceCheck) {
    FilterFlag filterFlag = FilterFlag.fromInt(descriptor.getFilterFlag());
    ItemResponse itemResponse = ItemResponse.builder()
        .id(UUID.randomUUID().toString())
        .count(descriptor.getCount())
        .serverHandleId(descriptor.getServerHandleId())
        .text(descriptor.getText())
        .itemValue(descriptor.getItemValue())
        .weight(descriptor.getWeight())
        .itemLevel(descriptor.getItemLevel())
        .numLegendaryStars(descriptor.getNumLegendaryStars())
        .isTradable(descriptor.isTradable())
        .isSpoiled(descriptor.isSpoiled())
        .isSetItem(descriptor.isSetItem())
        .isQuestItem(descriptor.isQuestItem())
        .isLegendary(descriptor.isLegendary())
        .vendingData(descriptor.getVendingData())
        .filterFlag(filterFlag)
        .itemDetails(buildItemDetails(descriptor, ownerInfo, itemSource))
        .isLearnedRecipe(descriptor.isLearnedRecipe())
        .canGoIntoScrapStash(descriptor.isCanGoIntoScrapStash())
        .isWeightless(descriptor.isWeightless())
        .scrapAllowed(descriptor.isScrapAllowed())
        .isAutoScrappable(descriptor.isAutoScrappable())
        .build();
    itemResponse.setPriceCheckResponse(getPriceCheck(itemResponse, autoPriceCheck));
    itemResponse.setFilterFlag(clarifyFilterFlag(itemResponse, descriptor));
    if (ENABLE_PRICE_ENHANCE && isValidForPriceCheckEnhance(itemResponse)) {
      PriceEnhanceRequest request = fed76Service.createPriceEnhanceRequest(itemResponse);
      if (request != null) {
        Response response = fed76Service.enhancePriceCheck(request);
        log.info("Price enhance response: {} for item {}", response, itemResponse);
      } else {
        log.error("Item matches price enhance, but has missing data: {}", itemResponse);
      }
    }
    return itemResponse;
  }

  private BasePriceCheckResponse getPriceCheck(ItemResponse itemResponse, boolean autoPriceCheck) {
    BasePriceCheckResponse priceCheckResponse = new BasePriceCheckResponse();
    if (autoPriceCheck) {
      if (itemResponse.getIsLegendary() && itemResponse.getIsTradable() && SUPPORTED_PRICE_CHECK_ITEMS
          .contains(itemResponse.getFilterFlag())) {
        PriceCheckRequest request = fed76Service.createPriceCheckRequest(itemResponse);
        if (request.isValid()) {
          priceCheckResponse = fed76Service.priceCheck(request);
        } else {
          log.error("Request is invalid, ignoring: {}\r\n{}", request, itemResponse);
        }
      }
    }
    return priceCheckResponse;
  }

  private ItemConfig findItemConfig(ItemDescriptor descriptor) {
    // TODO: add Status field to ItemConfig - FOUND, NON_LEGENDARY, NON_TRADABLE, NOT_FOUND, etc.
    if (shouldConvertItemName(descriptor) && descriptor.isTradable()) {
      switch (descriptor.getFilterFlagEnum()) {
        case NOTES:
          return gameConfigService.findPlanConfig(descriptor);
        case ARMOR:
        case POWER_ARMOR:
        case APPAREL:
          return gameConfigService.findArmorConfig(descriptor);
        default:
          return gameConfigService.findWeaponConfig(descriptor);
      }
    }
    return null;
  }

  private ItemDetails buildItemDetails(ItemDescriptor descriptor, OwnerInfo ownerInfo, ItemSource itemSource) {
    List<Pair<ItemCardEntry, ItemCardText>> pairs = getItemCardTexts(descriptor.getItemCardEntries());
    List<LegendaryMod> legendaryMods = pairs
        .stream()
        .filter(Objects::nonNull)
        .map(x -> getLegendaryMods(x, descriptor.getFilterFlagEnum()))
        .flatMap(List::stream)
        .collect(Collectors.toList());
    String abbreviation = buildLegendaryAbbreviation(legendaryMods, LegendaryMod::getAbbreviation);
    String abbreviationId = buildLegendaryAbbreviation(legendaryMods, LegendaryMod::getGameId);
    double packWeight = Objects.isNull(descriptor.getWeight()) ? 0 : descriptor.getWeight();

//    FilterFlag filterFlag = descriptor.getFilterFlagEnum();
    ItemConfig config = findItemConfig(descriptor);
    String name = StringUtils.EMPTY;
    if (Objects.nonNull(config)) {
      name = getDefaultText(config.getTexts());
//      if (config.getType() == FilterFlag.NOTES) {
//        filterFlag = FilterFlag.PLANS;
//        config.setType(filterFlag);
//      }
    }
    List<Stats> stats = buildStats(pairs);
    ArmorConfig armorConfig = gameConfigService.findArmorType(stats, abbreviation);
    return ItemDetails
        .builder()
        .name(name)
        .description(buildItemDescription(pairs, descriptor.isLegendary()))
        .config(config)
        .abbreviation(abbreviation)
        .abbreviationId(abbreviationId)
        .armorConfig(armorConfig)
        .legendaryMods(legendaryMods)
        .itemSource(itemSource)
        .filterFlag(descriptor.getFilterFlagEnum())
//        .filterFlag(filteFlag)
        .stats(stats)
        .ownerInfo(ownerInfo)
        .totalWeight(packWeight * descriptor.getCount())
        .build();
  }

  private Stats buildItemStats(ItemCardEntry itemCardEntry, ItemCardText itemCardText) {
    if (IGNORED_CARDS.contains(itemCardText)) {
      return null;
    }
    return Stats
        .builder()
        .itemCardText(itemCardText)
        .damageType(itemCardEntry.getDamageTypeEnum())
        .value(itemCardEntry.getValue())
        .build();
  }

  private List<Pair<ItemCardEntry, ItemCardText>> getItemCardTexts(List<ItemCardEntry> itemCardEntries) {
    if (CollectionUtils.isEmpty(itemCardEntries)) {
      return new ArrayList<>();
    }
    return itemCardEntries
        .stream()
        .map(itemCardEntry -> new MutablePair<>(itemCardEntry, gameConfigService.findItemCardText(itemCardEntry)))
        .collect(Collectors.toList());
  }

  private String buildItemDescription(List<Pair<ItemCardEntry, ItemCardText>> pairs, boolean isLegendary) {
    if (CollectionUtils.isEmpty(pairs)) {
      return null;
    }
    for (Pair<ItemCardEntry, ItemCardText> pair : pairs) {
      ItemCardText itemCardText = pair.getValue();
      ItemCardEntry itemCardEntry = pair.getKey();
      if (itemCardText == ItemCardText.DESC && !isLegendary) {
        return itemCardEntry.getValue();
      }
    }
    return null;
  }

  private List<Stats> buildStats(List<Pair<ItemCardEntry, ItemCardText>> pairs) {
    List<Stats> statList = new ArrayList<>();
    if (CollectionUtils.isEmpty(pairs)) {
      return statList;
    }
    for (Pair<ItemCardEntry, ItemCardText> pair : pairs) {
      ItemCardText itemCardText = pair.getValue();
      ItemCardEntry itemCardEntry = pair.getKey();
      Stats stats = buildItemStats(itemCardEntry, itemCardText);
      if (stats != null) {
        statList.add(stats);
      }
    }
    return statList;
  }

  private List<LegendaryMod> getLegendaryMods(Pair<ItemCardEntry, ItemCardText> pair,
      FilterFlag filterFlag) {
    return getLegendaryMods(pair.getKey(), pair.getValue(), filterFlag);
  }

  private List<LegendaryMod> getLegendaryMods(ItemCardEntry itemCardEntry, ItemCardText itemCardText,
      FilterFlag filterFlag) {
    List<LegendaryMod> mods = new ArrayList<>();
    if (itemCardText == ItemCardText.DESC) {
      if (StringUtils.isBlank(itemCardEntry.getValue())) {
        return mods;
      }
      String[] strings = itemCardEntry.getValue().split("\n");
      for (String mod : strings) {
        String newMod = mod.trim();
        if (StringUtils.isBlank(newMod)) {
          continue;
        }
        LegendaryMod legendaryMod = new LegendaryMod();
        legendaryMod.setText(newMod);
        LegendaryModDescriptor descriptor = gameConfigService.findLegendaryModDescriptor(newMod, filterFlag);
        if (Objects.isNull(descriptor)) {
          descriptor = new LegendaryModDescriptor();
        }
        String abbreviation = descriptor.getAbbreviation();
        if (StringUtils.isBlank(abbreviation)) {
          abbreviation = "";
        }
        String gameId = descriptor.getGameId();
        if (StringUtils.isBlank(gameId)) {
          gameId = "";
        }
        String text = getDefaultText(descriptor.getTexts());
        legendaryMod.setAbbreviation(abbreviation);
        legendaryMod.setStar(descriptor.getStar());
        legendaryMod.setId(descriptor.getId());
        legendaryMod.setGameId(gameId);
        legendaryMod.setText(text);
        mods.add(legendaryMod);
      }
    }
    if (CollectionUtils.isNotEmpty(mods)) {
      mods.sort(Comparator.comparingInt(LegendaryMod::getStar));
    }
    return mods;
  }

  public List<ItemResponse> prepareModData(ModDataRequest request, boolean autoPriceCheck) {
    // TODO: add dedupe logic
    List<ItemResponse> responses = prepareModData(request.getModData(), request.getFilters(), autoPriceCheck);
    if (request.getFilters().isPriceCheckOnly()) {
      return responses.stream().filter(ItemConverterService::matchesPriceCheckOnly).collect(Collectors.toList());
    }
    return responses;
  }

}
