package com.roywasker.Factory_Management.Service

import com.roywasker.Factory_Management.model.User
import com.roywasker.Factory_Management.repository.UsersRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class UserService(
    private val usersRepository: UsersRepository
){
    fun findByUserName(userName: String): User? {
        val user = usersRepository.findByUserName(userName)?: return null
        return user
    }

    fun findAllUser(): List<User>? {
        val users = usersRepository.findAll()
        return users
    }

    fun getUserAction(userId: ObjectId): Map<String,Int?>? {
        val user = usersRepository.findById(userId).orElse(null)
        return mapOf("actionToday" to user?.actionToday)
    }

    fun updateUserAction(userId: ObjectId): User {
        val user = usersRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found")
        }

        val updateActionForToday= user.copy(
            actionToday = user.actionToday.minus(1)
        )

        return usersRepository.save(updateActionForToday)
    }

    fun resetUserAction(userId: ObjectId): User {
        val user = usersRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found")
        }

        val updateActionForToday= user.copy(
            actionToday = user.numOfActions
        )

        return usersRepository.save(updateActionForToday)
    }
}