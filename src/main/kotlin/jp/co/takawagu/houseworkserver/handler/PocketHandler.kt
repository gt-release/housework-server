package jp.co.takawagu.houseworkserver.handler

import jp.co.takawagu.houseworkserver.model.Pocket
import jp.co.takawagu.houseworkserver.model.PocketRequest
import jp.co.takawagu.houseworkserver.model.TotalPrice
import jp.co.takawagu.houseworkserver.model.UsedPrice
import jp.co.takawagu.houseworkserver.repository.UsedRepository
import jp.co.takawagu.houseworkserver.service.PocketService
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@Controller
class PocketHandler(private val pocketService: PocketService,
                    private val usedRepository: UsedRepository) {

    fun aggregate(req: ServerRequest) : Mono<ServerResponse> {
        val userName = req.queryParam("userName").orElse("")
        val yearMonth = YearMonth.parse(req.queryParam("selectedMonth").orElse("2020/01"),
                DateTimeFormatter.ofPattern("yyyy/MM"))
        return ServerResponse.ok().body(
                pocketService.aggregateData(PocketRequest(userName, yearMonth)) ,
                Pocket::class.java
        )
    }

    fun sum(req: ServerRequest) = ServerResponse.ok().body(pocketService.sum(), TotalPrice::class.java)

    fun addUsed(req: ServerRequest) = ServerResponse.ok().body(
            req.bodyToMono(UsedPrice::class.java).flatMap {usedRepository.save(it)}, UsedPrice::class.java
    )

    fun getAll(req: ServerRequest)  = ServerResponse.ok().body(usedRepository.findAllOrderByInsertTimeDesc(), UsedPrice::class.java)

    fun delete(req: ServerRequest) : Mono<ServerResponse> {
        val workId: Mono<UUID> = req.bodyToMono(String::class.java).map{ UUID.fromString(it)}
        return ServerResponse.ok().body(
                workId.delayUntil(usedRepository::deleteById), UUID::class.java
        )
    }
}