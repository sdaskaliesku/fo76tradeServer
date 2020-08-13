package com.manson.fo76.domain;

public class ModDataRequest {

	private ModData modData;
	private ItemsUploadFilters filters;

	public ModData getModData() {
		return modData;
	}

	public void setModData(ModData modData) {
		this.modData = modData;
	}

	public ItemsUploadFilters getFilters() {
		return filters;
	}

	public void setFilters(ItemsUploadFilters filters) {
		this.filters = filters;
	}
}
