package jp.co.takawagu.houseworkserver.handler

import jp.co.takawagu.houseworkserver.model.Work
import jp.co.takawagu.houseworkserver.repository.WorkRepository
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Controller
class WorkHandler(private val workRepository: WorkRepository) {

    fun getAll(req: ServerRequest)  = ServerResponse.ok().body(workRepository.findAllOrderByInsertTimeDesc(), Work::class.java)

    fun add(req: ServerRequest) = ServerResponse.ok().body(
            req.bodyToMono(Work::class.java).flatMap {workRepository.save(it)}, Work::class.java
    )

    fun delete(req: ServerRequest) : Mono<ServerResponse> {
        val workId: Mono<UUID> = req.bodyToMono(String::class.java).map{UUID.fromString(it)}
        return ServerResponse.ok().body(
                workId.delayUntil(workRepository::deleteById), UUID::class.java
        )
    }
}