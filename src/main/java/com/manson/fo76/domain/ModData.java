package com.manson.fo76.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.fo76.domain.items.ItemDescriptor;
import java.util.List;

public class ModData extends AbstractObject {
	@JsonProperty("InventoryList")
	private List<ItemDescriptor> inventoryList;
	private List<ItemDescriptor> playerInventory;
	private List<ItemDescriptor> stashInventory;
	private User user;

	public List<ItemDescriptor> getInventoryList() {
		return inventoryList;
	}

	public void setInventoryList(List<ItemDescriptor> inventoryList) {
		this.inventoryList = inventoryList;
	}

	public List<ItemDescriptor> getPlayerInventory() {
		return playerInventory;
	}

	public void setPlayerInventory(
			List<ItemDescriptor> playerInventory) {
		this.playerInventory = playerInventory;
	}

	public List<ItemDescriptor> getStashInventory() {
		return stashInventory;
	}

	public void setStashInventory(List<ItemDescriptor> stashInventory) {
		this.stashInventory = stashInventory;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
