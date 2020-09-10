package com.manson.fo76.web.api

import com.manson.fo76.domain.ModDataRequest
import com.manson.fo76.domain.UploadItemRequest
import com.manson.fo76.domain.dto.ItemDTO
import com.manson.fo76.service.ItemService
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import java.util.*

@RestController
@RequestMapping("/items")
class ItemsController @Autowired constructor(private val itemService: ItemService) {
    @ApiIgnore
    @GetMapping(value = ["/findAll"], produces = ["application/json"])
    fun findAll(@RequestParam(required = false) page: Int?,
                @RequestParam(required = false) size: Int?): Page<ItemDTO?>? {
        return itemService.findAll(createPageRequest(page, size))
    }

    @ApiIgnore
    @GetMapping(value = ["/findAllByOwnerId"], produces = ["application/json"])
    fun findAllByOwnerId(@RequestParam ownerId: String?, @RequestParam(required = false) page: Int?,
                         @RequestParam(required = false) size: Int?): Page<ItemDTO?>? {
        return itemService.findAllByOwnerId(ownerId, createPageRequest(page, size))
    }

    @ApiIgnore
    @GetMapping(value = ["/findAllByOwnerName"], produces = ["application/json"])
    fun findAllByOwnerName(@RequestParam ownerName: String?, @RequestParam(required = false) page: Int?,
                           @RequestParam(required = false) size: Int?): Page<ItemDTO?>? {
        return itemService.findAllByOwnerName(ownerName, createPageRequest(page, size))
    }

    @ApiIgnore
    @GetMapping(value = ["/findByIdAndOwnerId"], produces = ["application/json"])
    fun findByIdAndOwnerId(@RequestParam id: String?, @RequestParam ownerId: String?): ItemDTO? {
        return itemService.findByIdAndOwnerId(id, ownerId)
    }

    @ApiIgnore
    @GetMapping(value = ["/findByIdAndOwnerName"], produces = ["application/json"])
    fun findByIdAndOwnerName(@RequestParam id: String?, @RequestParam ownerName: String?): ItemDTO? {
        return itemService.findByIdAndOwnerName(id, ownerName)
    }

    @RequestMapping(value = ["/prepareModData"], consumes = ["application/json"], produces = ["application/json"], method = [RequestMethod.POST])
    fun prepareModData(@RequestBody modDataRequest: ModDataRequest): List<ItemDTO?>? {
        try {
            return itemService.prepareModData(modDataRequest)
        } catch (e: Exception) {
            LOGGER.error("Error while preparing mod data {}", modDataRequest, e)
        }
        return ArrayList()
    }

    @ApiIgnore
    @PostMapping(value = ["/upload"], consumes = ["application/json"], produces = ["application/json"])
    fun prepareModData(@RequestBody uploadItemRequest: UploadItemRequest): ResponseEntity<Any> {
        if (Objects.isNull(uploadItemRequest)) {
            return ResponseEntity.noContent().build()
        }
        try {
            itemService.addItems(uploadItemRequest.user, uploadItemRequest.items!!)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            LOGGER.error("Error while uploading items: {}", uploadItemRequest, e)
        }
        return ResponseEntity.noContent().build()
    }

    @ApiIgnore
    @PostMapping(value = ["/delete"], consumes = ["application/json"], produces = ["application/json"])
    fun delete(@RequestBody items: List<ItemDTO?>): ResponseEntity<Any> {
        if (CollectionUtils.isEmpty(items)) {
            return ResponseEntity.noContent().build()
        }
        try {
            itemService.deleteItems(items)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResponseEntity.noContent().build()
    }

    @ApiIgnore
    @DeleteMapping(value = ["/deleteAll"], produces = ["application/json"])
    fun deleteAll(@RequestParam userId: String?): ResponseEntity<Any> {
        if (StringUtils.isEmpty(userId)) {
            return ResponseEntity.noContent().build()
        }
        try {
            itemService.deleteAllByOwnerId(userId)
            return ResponseEntity.ok().build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResponseEntity.noContent().build()
    }

    //  @PostMapping (value = "/contact", consumes = "application/x-amf")
    @GetMapping(value = ["/contact"], consumes = ["application/x-amf"])
    fun postContact(o: Any?) {
        println(o)
        //store the contact...
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ItemsController::class.java)
        private fun createPageRequest(page: Int?, size: Int?): PageRequest? {
            var pageRequest: PageRequest? = null
            if (page != null && size != null) {
                pageRequest = PageRequest.of(page, size)
            }
            return pageRequest
        }
    }
}