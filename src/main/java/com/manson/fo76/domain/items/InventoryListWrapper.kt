package com.manson.fo76.domain.items

import com.fasterxml.jackson.annotation.JsonProperty
import com.manson.fo76.domain.AbstractObject

class InventoryListWrapper : AbstractObject() {
    @JsonProperty("InventoryList")
    var inventoryList: List<ItemDescriptor>? = null
}