package com.roywasker.Factory_Management.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {

    private val bcrypt = BCryptPasswordEncoder()

    fun encode(rawPassword: String): String {
        require(rawPassword.toByteArray().size <= 72) {
            "Password cannot be more than 72 bytes"
        }
        return BCryptPasswordEncoder().encode(rawPassword)
    }

//    fun encode(row: String) : String = bcrypt.encode(row)

    fun matches(row: String, hashed: String): Boolean = bcrypt.matches(row, hashed)
}