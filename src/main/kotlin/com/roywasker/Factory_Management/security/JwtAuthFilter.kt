package com.roywasker.Factory_Management.security

import com.roywasker.Factory_Management.Service.JwtService
import com.roywasker.Factory_Management.Service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.bson.types.ObjectId
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Date

@Component
class JwtAuthFilter (
    private val jwtService: JwtService,
    private val userService: UserService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            if (jwtService.validateAccessToken(authHeader)) {
                val userId = jwtService.getUerIdFromToken(authHeader)

                val user = userService.getUserById(ObjectId(userId))

                if (user.actionDate != Date().toInstant().toString().substring(0, 10)) {
                    userService.resetUserAction(ObjectId(userId))
                }else if(user.actionToday == 0){
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.writer.write("No more actions for today")
                    return
                }

                userService.updateUserAction(ObjectId(userId))

                val auth = UsernamePasswordAuthenticationToken(userId, null, emptyList())
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        filterChain.doFilter(request, response)
    }
}