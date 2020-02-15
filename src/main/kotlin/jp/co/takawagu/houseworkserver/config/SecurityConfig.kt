package jp.co.takawagu.houseworkserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@Configuration
class SecurityConfig(private val authenticationManager: JwtAuthenticationManager,
                     private val securityContextRepository: ServerSecurityContextRepository) {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.cors().and()
                .exceptionHandling()
                .authenticationEntryPoint(EntryPoint())
                .accessDeniedHandler(DeniedHandler())
                .and()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/auth/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowCredentials = true
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "DELETE", "PUT")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    inner class EntryPoint : ServerAuthenticationEntryPoint {
        override fun commence(exchange: ServerWebExchange?, e: AuthenticationException?): Mono<Void>? {
            return Mono.fromRunnable {
                exchange?.response?.statusCode = HttpStatus.UNAUTHORIZED
            }
        }
    }

    inner class DeniedHandler : ServerAccessDeniedHandler {
        override fun handle(exchange: ServerWebExchange?, e: AccessDeniedException?): Mono<Void>? {
            return Mono.fromRunnable {
                exchange?.response?.statusCode = HttpStatus.FORBIDDEN
            }
        }
    }
}