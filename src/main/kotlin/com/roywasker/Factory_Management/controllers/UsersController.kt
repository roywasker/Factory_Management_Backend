package com.roywasker.Factory_Management.controllers

import com.roywasker.Factory_Management.Service.UserService
import com.roywasker.Factory_Management.model.User
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UsersController (
    val userService: UserService
) {

    data class  UserResponse(
        val id: String,
        val username: String,
        val userId: String,
        val fullName: String,
        val numOfActions: Int,
        val actionDate: String,
        val actionToday: Int
    )

    data class UserIdRequest(val userId: String)

    @GetMapping("/{userName}")
    fun findByUserName(@PathVariable userName: String): UserResponse? {
        return userService.findByUserName(userName)?.toUserResponse()
    }

    @GetMapping
    fun findAllUser(): List<UserResponse>? {
        val users = userService.findAllUser()?.map {
           it.toUserResponse()
        }
        return users
    }

    @GetMapping("/actions/{userId}")
    fun getUserAction(@PathVariable userId: ObjectId): Map<String,Int?>? {
        return userService.getUserAction(userId)
    }

    @PostMapping("/actions")
    fun updateUserAction(@RequestBody body: UserIdRequest): UserResponse {
        return userService.updateUserAction(ObjectId(body.userId)).toUserResponse()
    }

    @PostMapping("/actions/reset")
    fun resetUserAction(@RequestBody body: UserIdRequest): UserResponse {
       return userService.resetUserAction(ObjectId(body.userId)).toUserResponse()
    }

    private fun User.toUserResponse(): UserResponse {
        return UserResponse(
            id = id.toString(),
            userId = userId,
            fullName = fullName,
            numOfActions = numOfActions,
            actionDate = actionDate,
            actionToday = actionToday,
            username = userName,
        )
    }
}