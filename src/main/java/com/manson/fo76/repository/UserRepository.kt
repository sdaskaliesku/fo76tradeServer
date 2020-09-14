package com.manson.fo76.repository

import com.manson.fo76.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Service

@Service
interface UserRepository : MongoRepository<User?, String?> {
    fun findByName(name: String?): User?
    fun deleteByName(name: String?): User?
}