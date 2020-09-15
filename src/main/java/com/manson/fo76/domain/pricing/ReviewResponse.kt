package com.manson.fo76.domain.pricing

import com.manson.fo76.domain.AbstractObject

class ReviewResponse : AbstractObject() {
    var author: AuthorResponse = AuthorResponse()
    var dateCreated: String = "EMPTY"
    var description: String = "EMPTY"
    var name: String = "EMPTY"
    var reviewRating: ReviewRatingResponse = ReviewRatingResponse()
    var url: String = "EMPTY"
    override fun toString(): String {
        return "ReviewResponse(author=$author, dateCreated='$dateCreated', description='$description', name='$name', reviewRating=$reviewRating, url='$url')"
    }


}