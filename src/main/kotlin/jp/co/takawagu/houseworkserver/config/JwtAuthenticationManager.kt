package jp.co.takawagu.houseworkserver.config

import org.slf4j.Logger
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.RuntimeException

@Component
class JwtAuthenticationManager(private val jwtTokenUtil: JwtTokenUtil,
                               private val log : Logger):
ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return try {
            val authToken = authentication.credentials.toString()
            val username = jwtTokenUtil.getUserNameFromToken(authToken)
            if(!jwtTokenUtil.isTokenExpired(authToken) ) {
                val auth = UsernamePasswordAuthenticationToken(username, username, listOf())
                Mono.just(auth)
            } else {
                Mono.empty()
            }
        } catch (e: RuntimeException) {
            log.warn("Wrong Token.", e)
            Mono.empty()
        }
    }
}