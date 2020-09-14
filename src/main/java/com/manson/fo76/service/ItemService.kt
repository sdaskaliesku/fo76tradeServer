package com.manson.fo76.service

import com.manson.fo76.domain.ModDataRequest
import com.manson.fo76.domain.User
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.domain.dto.OwnerInfo
import com.manson.fo76.helper.Utils
import com.manson.fo76.repository.ItemRepository
import java.util.Objects
import java.util.stream.Collectors
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ItemService @Autowired constructor(
        private val itemRepository: ItemRepository, private val userService: UserService,
        private val itemConverterService: ItemConverterService,
) {
    fun findAll(pageable: Pageable?): Page<ItemDTO?> {
        return if (pageable == null) {
            PageImpl(itemRepository.findAll())
        } else itemRepository.findAll(pageable)
    }

    fun findAllByOwnerId(ownerId: String?, pageable: Pageable?): Page<ItemDTO?>? {
        return if (Objects.isNull(pageable)) {
            PageImpl(itemRepository.findAllByOwnerInfoId(ownerId))
        } else itemRepository.findAllByOwnerInfoId(ownerId, pageable)
    }

    fun findAllByOwnerName(ownerName: String?, pageable: Pageable?): Page<ItemDTO?>? {
        return if (Objects.isNull(pageable)) {
            PageImpl(itemRepository.findAllByOwnerInfoName(ownerName))
        } else itemRepository.findAllByOwnerInfoName(ownerName, pageable)
    }

    fun findByIdAndOwnerId(id: String?, ownerId: String?): ItemDTO? {
        return itemRepository.findByIdAndOwnerInfoId(id, ownerId)
    }

    fun findByIdAndOwnerName(id: String?, ownerName: String?): ItemDTO? {
        return itemRepository.findByIdAndOwnerInfoName(id, ownerName)
    }

    fun deleteAllByOwnerId(ownerId: String?): Boolean {
        itemRepository.deleteAllByOwnerInfoId(ownerId)
        return true
    }

    fun deleteItems(items: List<ItemDTO?>) {
        itemRepository.deleteAll(items)
    }

    fun addItem(userId: String?, itemDTO: ItemDTO): ItemDTO? {
        val user = userService.findByIdOrName(userId)
        return addItem(user, itemDTO)
    }

    fun addItem(user: User?, itemDTO: ItemDTO): ItemDTO? {
        if (Objects.isNull(user)) {
            return null
        }
        val userInDb = userService.findByIdOrName(user!!.id)
        if (!Utils.validatePassword(user, userInDb)) {
            return null
        }
        var ownerInfo = itemDTO.ownerInfo
        if (Objects.isNull(ownerInfo)) {
            ownerInfo = OwnerInfo()
        }
        ownerInfo!!.name = user.name
        ownerInfo.id = user.id
        itemDTO.ownerInfo = ownerInfo
        return itemRepository.save(itemDTO)
    }

    fun addItems(userId: String?, itemsDTO: List<ItemDTO>): MutableList<ItemDTO?>? {
        return itemsDTO.stream().map { itemDTO: ItemDTO -> addItem(userId, itemDTO) }.collect(Collectors.toList())
    }

    fun addItems(user: User, itemsDTO: List<ItemDTO>): MutableList<ItemDTO?>? {
        return itemsDTO.stream().map { itemDTO: ItemDTO -> addItem(user, itemDTO) }.filter(Objects::nonNull).collect(Collectors.toList())
    }

    fun findAllByOwnerId(user: User, pageable: Pageable?): Page<ItemDTO?>? {
        return findAllByOwnerId(user.id, pageable)
    }

    fun findAllByOwnerName(user: User, pageable: Pageable?): Page<ItemDTO?>? {
        return findAllByOwnerName(user.name, pageable)
    }

    fun prepareModData(modDataRequest: ModDataRequest): List<ItemDTO?>? {
        return itemConverterService.prepareModData(modDataRequest)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ItemService::class.java)
    }
}