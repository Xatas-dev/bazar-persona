package org.bazar.persona.adapter.outbound.user_avatar

import org.bazar.persona.domain.user_avatar.UserAvatar
import org.bazar.persona.domain.user_avatar.UserAvatarStatus

fun UserAvatar.toUserAvatarEntity() = UserAvatarEntity(
    userId, fileId, isActive, status.name, url, errorReason, createdAt, updatedAt, id
)

fun UserAvatarEntity.toUserAvatar() = UserAvatar(
    userId, fileId, UserAvatarStatus.valueOf(status), isActive, errorReason, createdAt, updatedAt, url, id
)