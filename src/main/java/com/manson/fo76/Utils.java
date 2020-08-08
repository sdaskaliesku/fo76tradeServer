package com.manson.fo76;

import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.StatsDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.item_card.ItemCardEntry;
import com.manson.fo76.service.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public abstract class Utils {

	private Utils() {
	}

	public static ItemDTO convertItem(ItemDescriptor item, User user) {
		Map<String, Object> objectMap = JsonParser.objectToMap(item);
		objectMap.put("filterFlag", item.getFilterFlagEnum());
		ItemDTO itemDTO = JsonParser.mapToItemDTO(objectMap);
		itemDTO.setOwnerId(user.getId());
		itemDTO.setOwnerName(user.getName());
		List<StatsDTO> stats = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(item.getItemCardEntries())) {
			stats = item.getItemCardEntries().stream().map(Utils::convertItemStats)
					.collect(Collectors.toList());
		}
		itemDTO.setStats(stats);
		return itemDTO;
	}

	public static StatsDTO convertItemStats(ItemCardEntry itemCardEntry) {
		Map<String, Object> objectMap = JsonParser.objectToMap(itemCardEntry);
		objectMap.put("textDecoded", itemCardEntry.getItemCardText());
		objectMap.put("damageType", itemCardEntry.getDamageTypeEnum());
		return JsonParser.mapToStatsDTO(objectMap);
	}

}
