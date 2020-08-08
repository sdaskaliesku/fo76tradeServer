package com.manson.fo76.domain.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.fo76.domain.AbstractObject;
import java.util.List;

public class InventoryListWrapper extends AbstractObject {

	@JsonProperty("InventoryList")
	private List<ItemDescriptor> inventoryList;

	public List<ItemDescriptor> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<ItemDescriptor> inventoryList) {
		this.inventoryList = inventoryList;
	}
}
