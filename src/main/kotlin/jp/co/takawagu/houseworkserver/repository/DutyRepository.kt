package jp.co.takawagu.houseworkserver.repository

import jp.co.takawagu.houseworkserver.model.Duty
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface DutyRepository : ReactiveCrudRepository<Duty, UUID> {
    @Query("select * from duty order by insert_time")
    fun findAllOrderByInsertTimeAsc(): Flux<Duty>
}