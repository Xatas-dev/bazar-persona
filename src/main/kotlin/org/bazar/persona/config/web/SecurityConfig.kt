package org.bazar.persona.config.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
    @Value($$"${management.server.port}") private val managementPort: Int,
    @Value($$"${server.port}") private val serverPort: Int
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher { request -> request.localPort == serverPort }
            .csrf { csrf ->
                csrf.disable()
            }
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/actuator/health").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { }
            }

        return http.build()
    }

    @Bean
    fun publicSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher { request ->
                request.localPort == managementPort
            }
            .csrf { csrf ->
                csrf.disable()
            }
            .authorizeHttpRequests { authz ->
                authz
                    .anyRequest().permitAll()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }

}