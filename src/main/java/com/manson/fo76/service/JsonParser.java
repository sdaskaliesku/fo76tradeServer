package com.manson.fo76.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.AppConfig;
import com.manson.fo76.domain.ModData;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.StatsDTO;
import java.io.File;
import java.util.Map;

public abstract class JsonParser {

	private static final TypeReference<Map<String, Object>> TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {
	};

	private static final ObjectMapper OM = AppConfig.getObjectMapper();

	private JsonParser() {
	}

	public static Map<String, Object> objectToMap(Object o) {
		return OM.convertValue(o, TYPE_REFERENCE);
	}

	public static ItemDTO mapToItemDTO(Map<String, Object> map) {
		return OM.convertValue(map, ItemDTO.class);
	}

	public static StatsDTO mapToStatsDTO(Map<String, Object> map) {
		return OM.convertValue(map, StatsDTO.class);
	}

	public static ModData parse(File file) {
		try {
			return OM.readValue(file, ModData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
