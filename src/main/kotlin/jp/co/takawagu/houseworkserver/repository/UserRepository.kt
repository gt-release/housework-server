package jp.co.takawagu.houseworkserver.repository

import jp.co.takawagu.houseworkserver.model.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface UserRepository : ReactiveCrudRepository<User, UUID> {

    @Query("select * from user_info where user_name = $1")
    fun findByUserName(userName: String): Mono<User>

    @Query("select user_id, user_name, color from user_info order by user_id")
    fun getAllUser(): Flux<User>
}