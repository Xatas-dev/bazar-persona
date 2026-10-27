package org.bazar.persona.config

import org.bazar.persona.BazarPersonaApplication
import org.bazar.persona.config.SharedAppContext.postgres
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase

@SpringBootTest(classes = [BazarPersonaApplication::class])
@ActiveProfiles("test")
@Sql("classpath:db/scripts/clearTables.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
abstract class BaseIntegrationTest {

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {

            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

}