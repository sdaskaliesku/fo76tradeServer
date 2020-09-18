package com.manson.fo76.domain.fed76

import com.manson.fo76.domain.items.ItemDescriptor

class FedModDataRequest {
    var version: Double = 0.0
    var characterInventories: Map<String, List<ItemDescriptor>> = HashMap()
}