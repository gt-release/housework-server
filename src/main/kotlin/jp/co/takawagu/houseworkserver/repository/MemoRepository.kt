package jp.co.takawagu.houseworkserver.repository

import jp.co.takawagu.houseworkserver.model.Memo
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface MemoRepository : ReactiveCrudRepository<Memo, UUID> {
    @Query("select * from memo order by insert_time desc")
    fun findAllOrderByInsertTimeDesc(): Flux<Memo>
}