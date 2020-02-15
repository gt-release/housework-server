package jp.co.takawagu.houseworkserver.handler

import jp.co.takawagu.houseworkserver.model.Duty
import jp.co.takawagu.houseworkserver.repository.DutyRepository
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Controller
class DutyHandler(private val dutyRepository: DutyRepository) {

    fun getAll(req: ServerRequest)  = ServerResponse.ok().body(dutyRepository.findAllOrderByInsertTimeAsc(), Duty::class.java)

    fun add(req: ServerRequest) = ServerResponse.ok().body(
            req.bodyToMono(Duty::class.java).flatMap {dutyRepository.save(it)}, Duty::class.java
    )

    fun delete(req: ServerRequest) : Mono<ServerResponse> {
        val dutyId: Mono<UUID> = req.bodyToMono(String::class.java).map{ UUID.fromString(it)}
        return ServerResponse.ok().body(
                dutyId.delayUntil(dutyRepository::deleteById), UUID::class.java
        )
    }
}