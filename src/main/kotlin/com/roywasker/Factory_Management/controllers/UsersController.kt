package com.roywasker.Factory_Management.controllers

import com.roywasker.Factory_Management.model.User
import com.roywasker.Factory_Management.repository.UsersRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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