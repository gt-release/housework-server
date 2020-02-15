package jp.co.takawagu.houseworkserver.repository

import jp.co.takawagu.houseworkserver.model.UsedPrice
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface UsedRepository : ReactiveCrudRepository<UsedPrice, UUID> {

    @Query("select * from used_price order by used_date desc, insert_time desc")
    fun findAllOrderByInsertTimeDesc(): Flux<UsedPrice>

    @Query("select * from used_price where user_name = $1 order by used_date")
    fun findAllByUserNameOrderByUsedDate(userName: String): Flux<UsedPrice>
}