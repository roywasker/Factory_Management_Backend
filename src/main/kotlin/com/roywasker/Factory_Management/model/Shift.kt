package com.roywasker.Factory_Management.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field


@Document("shifts")
data class Shift (
    @Id val id: ObjectId = ObjectId(),
    @Field("Date") val date: String,
    @Field("StartingHour") val startingHour: Int,
    @Field("EndingHour") val endingHour: Int,
    @Field("Employees") val employees: List<String>
)