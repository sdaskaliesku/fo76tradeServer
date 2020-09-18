package com.manson.fo76.domain

import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.User

class UploadItemRequest {
    var user: User = User()
    var items: List<ItemDTO> = listOf()
}