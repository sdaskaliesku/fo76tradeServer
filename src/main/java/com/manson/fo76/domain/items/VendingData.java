package com.manson.fo76.domain.items;

import com.manson.fo76.domain.AbstractObject;

public class VendingData extends AbstractObject {

	private Boolean isVendedOnOtherMachine;
	private Integer price;
	private Integer machineType;

	public Boolean getVendedOnOtherMachine() {
		return isVendedOnOtherMachine;
	}

	public void setVendedOnOtherMachine(Boolean vendedOnOtherMachine) {
		isVendedOnOtherMachine = vendedOnOtherMachine;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getMachineType() {
		return machineType;
	}

	public void setMachineType(Integer machineType) {
		this.machineType = machineType;
	}
}
