package com.roywasker.Factory_Management.repository

import com.roywasker.Factory_Management.model.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, ObjectId> {
    fun findByUserIdAndHashToken(userId: ObjectId, hashToken: String): RefreshToken?
    fun deleteByUserIdAndHashToken(userId: ObjectId, hashToken: String)
}