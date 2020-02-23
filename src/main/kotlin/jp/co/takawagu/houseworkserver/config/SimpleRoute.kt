package jp.co.takawagu.houseworkserver.config

import jp.co.takawagu.houseworkserver.handler.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router


@Configuration
class SimpleRoute(private val dutyHandler: DutyHandler,
                  private val workHandler: WorkHandler,
                  private val pocketHandler: PocketHandler,
                  private val authHandler: AuthHandler,
                  private val memoHandler: MemoHandler
) {

    @Bean
    fun route() = router {
        accept(APPLICATION_JSON).nest {
            GET("/duty").invoke(dutyHandler::getAll)
            POST("/duty/add").invoke(dutyHandler::add)
            DELETE("/duty/delete").invoke(dutyHandler::delete)
            GET("/work").invoke(workHandler::getAll)
            POST("/work/add").invoke(workHandler::add)
            DELETE("/work/delete").invoke(workHandler::delete)
            GET("/pocket/aggregate").invoke(pocketHandler::aggregate)
            GET("/pocket/sum").invoke(pocketHandler::sum)
            GET("/used").invoke(pocketHandler::getAll)
            POST("/used/add").invoke(pocketHandler::addUsed)
            DELETE("/used/delete").invoke(pocketHandler::delete)
            GET("/memo").invoke(memoHandler::getAll)
            POST("/memo/add").invoke(memoHandler::add)
            DELETE("/memo/delete").invoke(memoHandler::delete)
        }
    }

    @Bean
    fun authRoute() = router {
        accept(APPLICATION_JSON).nest {
            GET("/auth/user").invoke(authHandler::getAllUser)
            POST("/auth/login").invoke(authHandler::login)
//            POST("/auth/signup").invoke(authHandler::signUp)
        }
    }
}