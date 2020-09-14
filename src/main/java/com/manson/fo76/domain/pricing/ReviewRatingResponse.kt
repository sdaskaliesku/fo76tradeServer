package com.manson.fo76.domain.pricing

import com.manson.fo76.domain.AbstractObject

class ReviewRatingResponse : AbstractObject() {
    var bestRating: String = ""
    var ratingValue: String = ""
    var worstRating: String = ""
    override fun toString(): String {
        return "ReviewRatingResponse(bestRating='$bestRating', ratingValue='$ratingValue', worstRating='$worstRating')"
    }

}