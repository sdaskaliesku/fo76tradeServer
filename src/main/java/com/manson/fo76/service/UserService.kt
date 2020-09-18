package com.manson.fo76.service

import com.manson.fo76.domain.dto.User
import com.manson.fo76.helper.Utils
import com.manson.fo76.repository.UserRepository
import java.util.Objects
import java.util.Optional
import java.util.UUID
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) : UserDetailsService {
    fun createNewUser(user: User): User? {
        val userInDb = userRepository.findByName(user.name)
        if (Objects.isNull(userInDb)) {
            user.password = UUID.randomUUID().toString()
            return userRepository.save(user)
        }
        return null
    }

    fun findById(id: String): User? {
        return if (StringUtils.isNotBlank(id)) {
            userRepository.findById(id).orElse(null)
        } else null
    }

    fun findByName(name: String?): User? {
        return if (StringUtils.isNotBlank(name)) {
            userRepository.findByName(name)
        } else null
    }

    fun findByIdOrName(idOrName: String?): User {
        return Optional.ofNullable(findById(idOrName!!)).orElseGet { userRepository.findByName(idOrName) }
    }

    fun findByIdOrName(user: User): User? {
        return if (Objects.nonNull(user)) {
            Optional.ofNullable(findByIdOrName(user.id))
                    .orElseGet { findByIdOrName(user.name) }
        } else null
    }

    fun findAll(): List<User?> {
        return userRepository.findAll()
    }

    fun deleteAll() {
        userRepository.deleteAll()
    }

    fun delete(user: User): User? {
        if (Objects.isNull(user)) {
            return null
        }
        val userInDb = findByIdOrName(user)
        if (Objects.isNull(userInDb)) {
            return null
        }
        if (StringUtils.equals(userInDb!!.password, user.password)) {
            userRepository.delete(userInDb)
            return userInDb
        }
        return null
    }

    override fun loadUserByUsername(name: String): UserDetails? {
        val user = findByName(name)
        return if (user != null) {
            org.springframework.security.core.userdetails.User(user.name, user.password,
                    listOf())
        } else null
    }

    fun loadUser(user: User): UserDetails? {
        if (Objects.isNull(user)) {
            return null
        }
        val userInDb = findByIdOrName(user)
        return if (userInDb != null && Utils.validatePassword(user, userInDb)) {
            org.springframework.security.core.userdetails.User(userInDb.name, userInDb.password,
                    listOf())
        } else null
    }
}