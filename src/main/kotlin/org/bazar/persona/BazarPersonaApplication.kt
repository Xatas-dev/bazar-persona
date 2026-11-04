package org.bazar.persona

import org.bazar.persona.infra.config.properties.ValidationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ValidationProperties::class)
class BazarPersonaApplication

fun main(args: Array<String>) {
    runApplication<BazarPersonaApplication>(*args)
}
