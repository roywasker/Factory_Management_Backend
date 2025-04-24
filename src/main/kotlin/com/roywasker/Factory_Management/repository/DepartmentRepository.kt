package com.roywasker.Factory_Management.repository

import com.roywasker.Factory_Management.model.Department
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface DepartmentRepository: MongoRepository<Department, ObjectId>  {
}