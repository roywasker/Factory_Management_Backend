package com.roywasker.Factory_Management.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("users")
data class User(
    @Id val id: ObjectId = ObjectId(),
    @Field("UserId") val userId: String = "null",
    @Field("UserName") val userName: String,
    @Field("hashedPassword") val hashedPassword: String = "null",
    @Field("FullName") val fullName: String,
    @Field("NumOfActions") val numOfActions: Int,
    @Field("ActionDate") val actionDate: String,
    @Field("ActionToday") val actionToday: Int
)
