package com.manson.fo76.repository

import com.manson.fo76.domain.dto.ItemDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface ItemRepository : MongoRepository<ItemDTO?, String?> {
    fun findAllByOwnerInfoId(ownerId: String?): List<ItemDTO?>?
    fun findAllByOwnerInfoId(ownerId: String?, pageable: Pageable?): Page<ItemDTO?>?
    fun findAllByOwnerInfoName(ownerName: String?): List<ItemDTO?>?
    fun findAllByOwnerInfoName(ownerName: String?, pageable: Pageable?): Page<ItemDTO?>?
    fun findByIdAndOwnerInfoId(id: String?, ownerId: String?): ItemDTO?
    fun findByIdAndOwnerInfoName(id: String?, ownerName: String?): ItemDTO?
    fun deleteAllByOwnerInfoId(ownerId: String?)
}