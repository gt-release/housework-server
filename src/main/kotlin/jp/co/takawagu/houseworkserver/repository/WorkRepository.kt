package jp.co.takawagu.houseworkserver.repository

import jp.co.takawagu.houseworkserver.model.Work
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface WorkRepository : ReactiveCrudRepository<Work, UUID> {
    @Query("select * from work order by work_date desc, insert_time desc")
    fun findAllOrderByInsertTimeDesc(): Flux<Work>

    @Query("select * from work where user_name = $1 order by work_date")
    fun findAllByUserNameOrderByWorkDate(userName: String): Flux<Work>

}