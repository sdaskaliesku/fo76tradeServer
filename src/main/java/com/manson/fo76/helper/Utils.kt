package com.manson.fo76.helper

import com.manson.fo76.domain.User
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory

object Utils {
    private val LOGGER = LoggerFactory.getLogger(Utils::class.java)

    fun validatePassword(user: User?, userInDb: User?): Boolean {
        return StringUtils.equals(user!!.password, userInDb!!.password)
    }
}