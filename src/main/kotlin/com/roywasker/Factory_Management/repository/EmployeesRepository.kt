package com.roywasker.Factory_Management.repository

import com.roywasker.Factory_Management.model.Employee
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface EmployeesRepository: MongoRepository<Employee, ObjectId> {
}