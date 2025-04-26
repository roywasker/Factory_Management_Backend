package com.roywasker.Factory_Management.repository

import com.roywasker.Factory_Management.model.Shift
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ShiftsRepository: MongoRepository<Shift, ObjectId> {
}