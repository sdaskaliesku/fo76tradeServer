package com.manson.fo76.domain

import com.manson.fo76.domain.dto.ItemDTO

class UploadItemRequest {
    var user: User? = null
    var items: List<ItemDTO>? = null
}