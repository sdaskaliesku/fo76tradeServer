package com.manson.fo76.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.fo76.domain.items.ItemDescriptor;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

	@Override
	public String toString() {
		return new ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("inventoryList", inventoryList)
				.append("playerInventory", playerInventory)
				.append("stashInventory", stashInventory)
				.append("user", user)
				.toString();
	}
}
