package com.roywasker.Factory_Management.Service

import com.roywasker.Factory_Management.model.RefreshToken
import com.roywasker.Factory_Management.model.User
import com.roywasker.Factory_Management.repository.RefreshTokenRepository
import com.roywasker.Factory_Management.repository.UsersRepository
import com.roywasker.Factory_Management.security.HashEncoder
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.Instant
import java.util.Date
import java.util.*


@Service
class AuthService (
    private val jwtService: JwtService,
    private val usersRepository: UsersRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    fun register (userName: String, password: String,
                  fullName: String, numOfActions: Int = 5): User {

        val user = usersRepository.findByUserName(userName)

        if (user != null){
            throw ResponseStatusException(HttpStatus.CONFLICT, "A user with that user name already exists")
        }

        return usersRepository.save(
            User(
                userName = userName ,
                hashedPassword = hashEncoder.encode(password),
                fullName = fullName,
                numOfActions = numOfActions,
                actionDate = Date().toInstant().toString().substring(0, 10),
                actionToday = 0
            )
        )
    }

    fun login(userName: String, password: String): TokenPair {
        val user = usersRepository.findByUserName(userName)
            ?: throw BadCredentialsException("Invalid credentials.")

        if (!hashEncoder.matches(password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        storeRefreshToken(userId = user.id , refreshToken = newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    @Transactional
    fun refreshToken(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val userId = jwtService.getUerIdFromToken(refreshToken)
        val user = usersRepository.findById(ObjectId(userId)).orElseThrow{
            ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")
        }

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashToken(userId = user.id, hashToken = hashed)
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(401), "Invalid refresh token.")


        refreshTokenRepository.deleteByUserIdAndHashToken(user.id, hashed)

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())

        storeRefreshToken(userId = user.id , refreshToken = newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    fun storeRefreshToken(userId: ObjectId, refreshToken: String) {
        val hashed = hashToken(refreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expirationDate = expiresAt,
                hashToken = hashed
            )
        )

    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

}