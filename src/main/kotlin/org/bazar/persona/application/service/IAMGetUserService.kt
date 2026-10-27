package org.bazar.persona.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.persona.adapter.inbound.user.toUserDtoResponse
import org.bazar.persona.adapter.inbound.user.toUserDtoResponseV2
import org.bazar.persona.application.shared.port.out.CurrentUserProvider
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.domain.user.User
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.model.UserDtoResponseV2
import org.springframework.stereotype.Service

@Service
class IAMGetUserService(
    private val userRepositoryPort: UserRepositoryPort,
    private val currentUserProvider: CurrentUserProvider
) {

    private val logger = KotlinLogging.logger { }

    @Deprecated("Deprecated since 1.0.1, will be removed in 1.0.3")
    fun execute(): UserDtoResponse {
        val userId = currentUserProvider.id
        val username = currentUserProvider.username
        return userRepositoryPort.findById(userId)?.toUserDtoResponse() ?: run {
            logger.info { "Haven't found user, saving new from jwt token, userId = $userId" }
            userRepositoryPort.insert(
                User(
                    id = userId,
                    username = username,
                    displayName = username
                )
            ).toUserDtoResponse()
        }
    }

    fun executeV2(): UserDtoResponseV2 {
        val userId = currentUserProvider.id
        val username = currentUserProvider.username
        return userRepositoryPort.findById(userId)?.toUserDtoResponseV2() ?: run {
            logger.info { "Haven't found user, saving new from jwt token, userId = $userId" }
            userRepositoryPort.insert(
                User(
                    id = userId,
                    username = username,
                    displayName = username
                )
            ).toUserDtoResponseV2()
        }
    }

}