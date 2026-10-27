package org.bazar.persona.config

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.lifecycle.Startables
import org.testcontainers.utility.DockerImageName
import java.util.stream.Stream

object SharedAppContext {

    val postgres: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:16.0"))
        .apply {
            this.withDatabaseName("testDb").withUsername("test").withPassword("test")
        }

    init {
        Startables.deepStart(Stream.of(postgres)).join()
    }

}