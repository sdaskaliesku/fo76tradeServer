package com.manson.fo76.domain.pricing

import com.manson.fo76.domain.AbstractObject

class AuthorResponse : AbstractObject() {
    var name: String = "FED76"
    var logo: String = "https://fed76.info/static/fed150.png"
    var description: String = "Battle-tested source of Fallout 76 information"
    var url: String = "https://fed76.info/"
    override fun toString(): String {
        return "AuthorResponse(name='$name', logo='$logo', description='$description', url='$url')"
    }


}