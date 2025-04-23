package com.roywasker.Factory_Management.repository

import com.roywasker.Factory_Management.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UsersRepository : MongoRepository<User, ObjectId> {
    fun findByUserName(userName: String): User?
}