package jp.co.takawagu.houseworkserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableR2dbcRepositories
@EnableWebFlux
@ConfigurationPropertiesScan
class HouseworkServerApplication

fun main(args: Array<String>) {
	runApplication<HouseworkServerApplication>(*args)
}
