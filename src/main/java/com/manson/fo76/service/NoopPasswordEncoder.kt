package com.manson.fo76.service

import org.apache.commons.lang3.StringUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class NoopPasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence): String {
        return rawPassword.toString()
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return StringUtils.equals(rawPassword.toString(), encodedPassword)
    }
}