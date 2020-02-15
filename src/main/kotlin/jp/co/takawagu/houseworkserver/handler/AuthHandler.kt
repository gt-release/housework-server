package jp.co.takawagu.houseworkserver.handler

import jp.co.takawagu.houseworkserver.config.JwtTokenUtil
import jp.co.takawagu.houseworkserver.model.LoginRequest
import jp.co.takawagu.houseworkserver.model.LoginResponse
import jp.co.takawagu.houseworkserver.model.User
import jp.co.takawagu.houseworkserver.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Controller
class AuthHandler(private val passwordEncoder: PasswordEncoder,
                  private val userRepository: UserRepository,
                  private val jwtTokenUtil: JwtTokenUtil) {
    fun signUp(req: ServerRequest): Mono<ServerResponse> {
        return req
                .bodyToMono(User::class.java)
                .map { it.password = passwordEncoder.encode(it.password)
                        return@map it }
                .flatMap { userRepository.findByUserName(it.userName)
                        .flatMap { ServerResponse.badRequest().build()}
                        .switchIfEmpty(userRepository.save(it).flatMap {
                            savedUser -> ServerResponse.ok().bodyValue(savedUser)
                        })
                }
    }

    fun login(req: ServerRequest): Mono<ServerResponse> {
        val loginRequest = req.bodyToMono(LoginRequest::class.java)
        return loginRequest.flatMap {
            userRepository.findByUserName(it.userName)
                    .flatMap { user ->
                        if(passwordEncoder.matches(it.password, user.password)) {
                            ServerResponse.ok().bodyValue(LoginResponse(jwtTokenUtil.generateToken(user)))
                        } else {
                            ServerResponse.badRequest().build()
                        }
                    }.switchIfEmpty(ServerResponse.badRequest().build())
        }
    }
}

