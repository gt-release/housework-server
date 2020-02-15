package jp.co.takawagu.houseworkserver.repository

import jp.co.takawagu.houseworkserver.config.JwtAuthenticationManager
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(private val authenticationManager: JwtAuthenticationManager)
    : ServerSecurityContextRepository
{
    companion object {
        const val TOKEN_PREFIX = "Bearer "
    }

    @Override
    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    @Override
    override fun load(exchange: ServerWebExchange?): Mono<SecurityContext> {
        val req = exchange?.request
        val authHeader = req?.headers?.getFirst(HttpHeaders.AUTHORIZATION)
        val authToken = if(authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            authHeader.replace(TOKEN_PREFIX, "")
        } else {
            null
        }
        return if(authToken != null) {
            val auth = UsernamePasswordAuthenticationToken(authToken, authToken)
            this.authenticationManager.authenticate(auth).map { SecurityContextImpl(it) }
        } else {
            Mono.empty()
        }
    }
}