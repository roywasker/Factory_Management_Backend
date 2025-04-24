package com.roywasker.Factory_Management.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("employees")
data class Employee(
    @Id val id: ObjectId = ObjectId(),
    @Field("FirstName") val firstName: String,
    @Field("LastName") val lastName: String,
    @Field("StartWorkYear") val startWorkYear: Int,
    @Field("DepartmentID") val departmentId: String,
)
