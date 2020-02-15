package jp.co.takawagu.houseworkserver.config

import jp.co.takawagu.houseworkserver.handler.AuthHandler
import jp.co.takawagu.houseworkserver.handler.DutyHandler
import jp.co.takawagu.houseworkserver.handler.PocketHandler
import jp.co.takawagu.houseworkserver.handler.WorkHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router


@Configuration
class SimpleRoute(private val dutyHandler: DutyHandler,
                  private val workHandler: WorkHandler,
                  private val pocketHandler: PocketHandler,
                  private val authHandler: AuthHandler) {

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
        }
    }

    @Bean
    fun authRoute() = router {
        accept(APPLICATION_JSON).nest {
            POST("/auth/login").invoke(authHandler::login)
//            POST("/auth/signup").invoke(authHandler::signUp)
        }
    }
}