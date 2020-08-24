package com.manson.fo76.domain;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ItemsUploadFilters {

	private List<Integer> filterFlags = new ArrayList<>();
	private boolean legendaryOnly;
	private boolean tradableOnly;

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

	public boolean isTradableOnly() {
		return tradableOnly;
	}

	public void setTradableOnly(boolean tradableOnly) {
		this.tradableOnly = tradableOnly;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, org.apache.commons.lang3.builder.ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("filterFlags", filterFlags)
				.append("legendaryOnly", legendaryOnly)
				.toString();
	}
}
