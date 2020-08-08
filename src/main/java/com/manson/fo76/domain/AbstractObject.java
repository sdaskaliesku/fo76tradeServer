package com.manson.fo76.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

public class AbstractObject {

	protected Map<String, Object> unknownFields = new HashMap<>();

	@JsonAnyGetter
	public Map<String, Object> getUnknownFields() {
		return unknownFields;
	}

	@JsonAnySetter
	public void setUnknownField(String key, Object value) {
		this.unknownFields.put(key, value);
	}
}
