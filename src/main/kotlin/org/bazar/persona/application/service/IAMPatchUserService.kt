package org.bazar.persona.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.persona.adapter.inbound.user.toUserDtoResponse
import org.bazar.persona.adapter.inbound.user.toUserDtoResponseV2
import org.bazar.persona.application.shared.port.out.CurrentUserProvider
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.domain.exceptions.DomainException
import org.bazar.persona.domain.exceptions.DomainExceptions
import org.bazar.persona.model.UpdateProfileDtoRequest
import org.bazar.persona.model.UpdateProfileDtoRequestV2
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.model.UserDtoResponseV2
import org.springframework.stereotype.Service

@Service
class IAMPatchUserService(
    private val userRepositoryPort: UserRepositoryPort,
    private val currentUserProvider: CurrentUserProvider
) {

    private val logger = KotlinLogging.logger { }

    @Deprecated("Deprecated since 1.0.1, will be removed in 1.0.3")
    fun updateUserInfo(dto: UpdateProfileDtoRequest): UserDtoResponse {
        val userId = currentUserProvider.id

        val user = userRepositoryPort.findById(userId) ?: run {
            logger.warn { "Tried to update user=$userId info, no such user found" }
            throw DomainException(DomainExceptions.USER_NOT_FOUND)
        }
        if (dto.userName == null) {
            return user.toUserDtoResponse()
        }
        logger.info { "Successfully updated user info for user = $userId" }
        return userRepositoryPort.update(user.update(dto.userName)).toUserDtoResponse()
    }

    fun updateUserInfoV2(dto: UpdateProfileDtoRequestV2): UserDtoResponseV2 {
        val userId = currentUserProvider.id
        val user = userRepositoryPort.findById(userId) ?: run {
            logger.warn { "Tried to update user=$userId info, no such user found" }
            throw DomainException(DomainExceptions.USER_NOT_FOUND)
        }
        logger.info { "Successfully updated user info for user = $userId" }
        return userRepositoryPort.update(user.update(dto.displayName)).toUserDtoResponseV2()
    }

}