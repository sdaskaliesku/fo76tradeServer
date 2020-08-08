package com.manson.fo76.domain;

import java.util.ArrayList;
import java.util.List;

public class ItemsUploadFilters {

	private List<Integer> filterFlags = new ArrayList<>();
	private boolean legendaryOnly = true;

	public List<Integer> getFilterFlags() {
		return filterFlags;
	}

	public void setFilterFlags(List<Integer> filterFlags) {
		this.filterFlags = filterFlags;
	}

	public boolean isLegendaryOnly() {
		return legendaryOnly;
	}

	public void setLegendaryOnly(boolean legendaryOnly) {
		this.legendaryOnly = legendaryOnly;
	}
}
