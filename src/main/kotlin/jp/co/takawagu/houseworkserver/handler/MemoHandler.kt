package jp.co.takawagu.houseworkserver.handler

import jp.co.takawagu.houseworkserver.model.Memo
import jp.co.takawagu.houseworkserver.repository.MemoRepository
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Controller
class MemoHandler(private val memoRepository: MemoRepository) {

    fun getAll(req: ServerRequest)  = ServerResponse.ok().body(memoRepository.findAllOrderByInsertTimeDesc(), Memo::class.java)

    fun add(req: ServerRequest) = ServerResponse.ok().body(
            req.bodyToMono(Memo::class.java).flatMap {memoRepository.save(it)}, Memo::class.java
    )

    fun delete(req: ServerRequest) : Mono<ServerResponse> {
        val memoId: Mono<UUID> = req.bodyToMono(String::class.java).map{ UUID.fromString(it)}
        return ServerResponse.ok().body(
                memoId.delayUntil(memoRepository::deleteById), UUID::class.java
        )
    }
}