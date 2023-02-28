package io.freevariable.auktion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AuktionApplication {

	@Bean
	fun projectionFactory() = org.springframework.data.projection.SpelAwareProxyProjectionFactory()
}

fun main(args: Array<String>) {
	runApplication<AuktionApplication>(*args)
}


