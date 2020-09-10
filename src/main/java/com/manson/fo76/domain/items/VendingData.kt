package com.manson.fo76.domain.items

import com.manson.fo76.domain.AbstractObject

class VendingData : AbstractObject() {
    var vendedOnOtherMachine: Boolean? = null
    var price: Int? = null
    var machineType: Int? = null
}