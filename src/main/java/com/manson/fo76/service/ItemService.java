package com.manson.fo76.service;

import com.manson.fo76.domain.ItemsUploadFilters;
import com.manson.fo76.domain.ModData;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.enums.FilterFlag;
import com.manson.fo76.repository.ItemRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

	private final ItemRepository itemRepository;

	@Autowired
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	private static void processItems(List<ItemDescriptor> items, Map<Long, ItemDescriptor> map,
			ItemsUploadFilters itemsUploadFilters) {
		for (ItemDescriptor itemDescriptor : items) {
			if (map.containsKey(itemDescriptor.getServerHandleId())) {
				continue;
			}
			if (!itemDescriptor.getTradable()) {
//				continue;
			}
			FilterFlag filterFlag = itemDescriptor.getFilterFlagEnum();
			boolean matchesFilter = false;
			if (filterFlag == null) {
				System.out.println("Empty filter flag");
				continue;
			} else {
				if (itemsUploadFilters.isLegendaryOnly()) {
					Integer stars = itemDescriptor.getNumLegendaryStars();
					if (stars == null || stars <= 0) {
						continue;
					}
				}
				for (Integer flag : filterFlag.getFlags()) {
					if (itemsUploadFilters.getFilterFlags().contains(flag)) {
						matchesFilter = true;
						break;
					}
				}
			}
			if (matchesFilter) {
				map.put(itemDescriptor.getServerHandleId(), itemDescriptor);
			}
		}
	}

	public List<ItemDTO> findAll() {
		return itemRepository.findAll();
	}

	public List<ItemDTO> findAllByOwnerId(String ownerId) {
		return itemRepository.findAllByOwnerId(ownerId);
	}

	public List<ItemDTO> findAllByOwnerName(String ownerName) {
		return itemRepository.findAllByOwnerName(ownerName);
	}

	public ItemDTO findByIdAndOwnerId(String id, String ownerId) {
		return itemRepository.findByIdAndOwnerId(id, ownerId);
	}

	public ItemDTO findByIdAndOwnerName(String id, String ownerName) {
		return itemRepository.findByIdAndOwnerName(id, ownerName);
	}

	public List<ItemDTO> findAllByOwnerId(User user) {
		return findAllByOwnerId(user.getId());
	}

	public List<ItemDTO> findAllByOwnerName(User user) {
		return findAllByOwnerName(user.getName());
	}

	public Pair<User, List<ItemDescriptor>> processModDataItems(File file,
			ItemsUploadFilters itemsUploadFilters) {
		ModData modData = JsonParser.parse(file);
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
