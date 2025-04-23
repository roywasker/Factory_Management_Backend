package com.roywasker.Factory_Management.controllers

import com.roywasker.Factory_Management.model.User
import com.roywasker.Factory_Management.repository.UsersRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UsersController (
    val usersRepository: UsersRepository
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
        val user = usersRepository.findByUserName(userName)?: return null
        return user.toUserResponse()
    }

    @GetMapping
    fun findAllUser(): List<UserResponse>? {
        val users = usersRepository.findAll().map {
           it.toUserResponse()
        }
        return users
    }

    @GetMapping("/actions/{userId}")
    fun getUserAction(@PathVariable userId: ObjectId): Map<String,Int?>? {
        val user = usersRepository.findById(userId).orElse(null)
        return mapOf("actionToday" to user?.actionToday)
    }

    @PostMapping("/actions")
    fun updateUserAction(@RequestBody body: UserIdRequest): UserResponse {
        val user = usersRepository.findById(ObjectId(body.userId)).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found")
        }

        val updateActionForToday= user.copy(
            actionToday = user.actionToday.minus(1)
        )

        return usersRepository.save(updateActionForToday).toUserResponse()
    }

    @PostMapping("/actions/reset")
    fun resetUserAction(@RequestBody body: UserIdRequest): UserResponse {
        val user = usersRepository.findById(ObjectId(body.userId)).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found")
        }

        val updateActionForToday= user.copy(
            actionToday = user.numOfActions
        )

        return usersRepository.save(updateActionForToday).toUserResponse()
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