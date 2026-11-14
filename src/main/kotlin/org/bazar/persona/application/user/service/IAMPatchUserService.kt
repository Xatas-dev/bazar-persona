package org.bazar.persona.application.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.persona.application.shared.port.out.CurrentUserProvider
import org.bazar.persona.application.user.model.UserWithAvatar
import org.bazar.persona.application.user.model.query.PatchUserProfileQuery
import org.bazar.persona.application.user.model.query.PatchUserProfileQueryV2
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.application.user_avatar.port.out.UserAvatarRepositoryPort
import org.bazar.persona.domain.exceptions.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class IAMPatchUserService(
    private val userRepositoryPort: UserRepositoryPort,
    private val currentUserProvider: CurrentUserProvider,
    private val userAvatarRepositoryPort: UserAvatarRepositoryPort
) {

    private val logger = KotlinLogging.logger { }

    @Deprecated("Deprecated since 1.0.1, will be removed in 1.0.3")
    fun updateUserInfo(dto: PatchUserProfileQuery): UserWithAvatar {
        val userId = currentUserProvider.id

        val user = userRepositoryPort.findById(userId) ?: run {
            logger.warn { "Tried to update user=$userId info, no such user found" }
            throw UserNotFoundException(userId)
        }
        if (dto.userName == null) {
            return user.toUserWithAvatar(null)
        }
        logger.info { "Successfully updated user info for user = $userId" }
        return userRepositoryPort.update(user.update(dto.userName)).toUserWithAvatar(null)
    }

    fun updateUserInfoV2(dto: PatchUserProfileQueryV2): UserWithAvatar {
        val userId = currentUserProvider.id
        val userAvatar = userAvatarRepositoryPort.findActiveAvatarByUserId(userId)
        val user = userRepositoryPort.findById(userId) ?: run {
            logger.warn { "Tried to update user=$userId info, no such user found" }
            throw UserNotFoundException(userId)
        }
        logger.info { "Successfully updated user info for user = $userId" }
        return userRepositoryPort.update(user.update(dto.displayName)).toUserWithAvatar(userAvatar)
    }

}