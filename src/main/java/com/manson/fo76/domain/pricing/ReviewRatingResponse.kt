package com.manson.fo76.domain.pricing

import com.manson.fo76.domain.AbstractObject

class ReviewRatingResponse : AbstractObject() {
    var bestRating: String = "EMPTY"
    var ratingValue: String = "EMPTY"
    var worstRating: String = "EMPTY"
    override fun toString(): String {
        return "ReviewRatingResponse(bestRating='$bestRating', ratingValue='$ratingValue', worstRating='$worstRating')"
    }

}