package com.manson.fo76.domain.pricing

import com.manson.fo76.domain.AbstractObject

class PriceCheckResponse : AbstractObject() {

    var name: String = ""
    var price: Int = 0
    var review: ReviewResponse = ReviewResponse()
    var timestamp: String = ""
    var path: String = ""
    var description: String = ""

    override fun toString(): String {
        return "PriceCheckResponse(name='$name', price=$price, review=$review, timestamp='$timestamp', path='$path', description='$description')"
    }


}