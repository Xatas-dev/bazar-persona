package org.bazar.persona

import org.bazar.persona.infra.config.properties.ValidationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableConfigurationProperties(ValidationProperties::class)
@EnableFeignClients
class BazarPersonaApplication

fun main(args: Array<String>) {
    runApplication<BazarPersonaApplication>(*args)
}
