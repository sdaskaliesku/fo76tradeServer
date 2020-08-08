package com.manson.fo76.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.domain.ModData;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.StatsDTO;
import com.manson.fo76.domain.items.InventoryListWrapper;
import com.manson.fo76.domain.items.ItemDescriptor;
import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class JsonParser {

	private static final ObjectMapper OM = new ObjectMapper();
	private static final TypeReference<List<ItemDescriptor>> typeReference = new TypeReference<List<ItemDescriptor>>() {
	};
	private static final TypeReference<Map<String, Object>> typeReference2 = new TypeReference<Map<String, Object>>() {
	};

	static {
		OM.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		OM.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}

	private JsonParser() {
	}

	public static ModData parse(File file) {
		try {
			return OM.readValue(file, ModData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public static List<ItemDescriptor> parse(File file) {
//		try {
//			return OM.readValue(file, typeReference);
//		} catch (Exception e) {
//			try {
//				return OM.readValue(file, InventoryListWrapper.class).getInventoryList();
//			} catch (Exception e1) {
//
//			}
//		}
//		return null;
//	}

	public static Map<String, Object> objectToMap(Object o) {
		return OM.convertValue(o, typeReference2);
	}

	public static ItemDTO mapToItemDTO(Map<String, Object> map) {
		return OM.convertValue(map, ItemDTO.class);
	}

	public static StatsDTO mapToStatsDTO(Map<String, Object> map) {
		return OM.convertValue(map, StatsDTO.class);
	}

}
