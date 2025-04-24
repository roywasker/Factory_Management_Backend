package com.roywasker.Factory_Management.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field


@Document("departments")
data class Department(
    @Id val id: ObjectId = ObjectId(),
    @Field("Name") val name: String,
    @Field("Manager") val manager: String
)
