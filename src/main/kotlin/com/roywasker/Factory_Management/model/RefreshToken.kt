package com.roywasker.Factory_Management.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("RefreshToken")
data class RefreshToken(
    val userId: ObjectId,
    @Indexed(expireAfter = "0s")
    val expirationDate: Instant,
    val createdAt: Instant = Instant.now(),
    val hashToken: String

    )
