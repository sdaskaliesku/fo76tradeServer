package com.manson.fo76.service

import com.manson.fo76.helper.Utils
import com.manson.fo76.repository.UserRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) : UserDetailsService {
    fun createNewUser(user: com.manson.fo76.domain.User): com.manson.fo76.domain.User? {
        val userInDb = userRepository.findByName(user.name)
        if (Objects.isNull(userInDb)) {
            user.password = UUID.randomUUID().toString()
            return userRepository.save(user)
        }
        return null
    }

    fun findById(id: String): com.manson.fo76.domain.User? {
        return if (StringUtils.isNotBlank(id)) {
            userRepository.findById(id).orElse(null)
        } else null
    }

    fun findByName(name: String?): com.manson.fo76.domain.User? {
        return if (StringUtils.isNotBlank(name)) {
            userRepository.findByName(name)
        } else null
    }

    fun findByIdOrName(idOrName: String?): com.manson.fo76.domain.User {
        return Optional.ofNullable(findById(idOrName!!)).orElseGet { userRepository.findByName(idOrName) }
    }

    fun findByIdOrName(user: com.manson.fo76.domain.User): com.manson.fo76.domain.User? {
        return if (Objects.nonNull(user)) {
            Optional.ofNullable(findByIdOrName(user.id))
                    .orElseGet { findByIdOrName(user.name) }
        } else null
    }

    fun findAll(): List<com.manson.fo76.domain.User?> {
        return userRepository.findAll()
    }

    fun deleteAll() {
        userRepository.deleteAll()
    }

    fun delete(user: com.manson.fo76.domain.User): com.manson.fo76.domain.User? {
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
            User(user.name, user.password,
                    ArrayList())
        } else null
    }

    fun loadUser(user: com.manson.fo76.domain.User): UserDetails? {
        if (Objects.isNull(user)) {
            return null
        }
        val userInDb = findByIdOrName(user)
        return if (userInDb != null && Utils.validatePassword(user, userInDb)) {
            User(userInDb.name, userInDb.password,
                    ArrayList())
        } else null
    }
}