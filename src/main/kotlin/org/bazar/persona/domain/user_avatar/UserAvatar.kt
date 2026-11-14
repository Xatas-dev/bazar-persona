package org.bazar.persona.domain.user_avatar

import java.time.Instant
import java.util.UUID

data class UserAvatar constructor(
    val userId: UUID,
    val fileId: UUID,
    val status: UserAvatarStatus,
    val isActive: Boolean,
    val errorReason: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val url: String? = null,
    val id: Long? = null
) {

    fun uploadSuccess(url: String): UserAvatar {
        return copy(
            status = UserAvatarStatus.SUCCESS,
            isActive = true,
            url = url,
            updatedAt = Instant.now()
        )
    }

    fun uploadFailed(errorReason: String) =
        copy(
            status = UserAvatarStatus.ERROR,
            errorReason = errorReason, updatedAt = Instant.now()
        )

    fun setActive(isActive: Boolean) = copy(isActive = isActive, updatedAt = Instant.now())


    companion object {
        fun createAvatarForUser(userId: UUID, fileId: UUID) =
            UserAvatar(
                userId, fileId, UserAvatarStatus.PENDING, isActive = false
            )
    }

}