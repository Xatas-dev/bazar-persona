package org.bazar.persona.application.user_avatar.port.out

import org.bazar.persona.domain.user_avatar.UserAvatar
import java.util.UUID

interface UserAvatarRepositoryPort {

    fun insert(avatar: UserAvatar) : UserAvatar

    fun update(avatar: UserAvatar): UserAvatar

    fun findActiveAvatarByUserId(isActive: Boolean, userId: UUID): UserAvatar?

    fun findAvatarByFileId(fileId: UUID): UserAvatar?

}