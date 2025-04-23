package com.roywasker.Factory_Management.controllers

import com.roywasker.Factory_Management.Service.AuthService
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController (
    private val authService: AuthService,
){
    data class AuthRegisterRequest(
        val userName: String,
        @field:Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
            message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character."
        )
        val password: String,
        val fullName: String,
        val numOfAction: Int
    )

    data class AuthLoginRequest(
        val userName: String,
        val password: String
    )

    data class RefreshRequest(
        val refreshToken: String
    )

    @PostMapping("/register")
    fun register (
        @Valid @RequestBody body: AuthRegisterRequest
    ): ResponseEntity<String> {
        authService.register(body.userName, body.password, body.fullName, body.numOfAction)
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully")
    }

    @PostMapping("/login")
    fun login (
        @RequestBody body: AuthLoginRequest
    ): ResponseEntity<AuthService.TokenPair> {
        val tokenPair = authService.login(body.userName, body.password)
        return ResponseEntity.status(200).body(tokenPair)
    }

    @PostMapping("/refresh")
    fun refresh (
        @RequestBody body: RefreshRequest
    ): AuthService.TokenPair {
        return authService.refreshToken(body.refreshToken)
    }
}