package org.bazar.persona.application.user.service

import org.bazar.persona.application.shared.port.out.CompleteUploadRequest
import org.bazar.persona.application.shared.port.out.GetUploadUrlRequest
import org.bazar.persona.application.user.model.UserWithAvatar
import org.bazar.persona.application.user.model.command.CompleteAvatarUploadCommand
import org.bazar.persona.application.user.model.command.InitiateAvatarUploadCommand
import org.bazar.persona.domain.user.User
import org.bazar.persona.domain.user_avatar.UserAvatar
import java.util.UUID

fun InitiateAvatarUploadCommand.toGetUploadUrlRequest(userId: UUID) =
    GetUploadUrlRequest(userId, fileName, sizeBytes, contentType, "bazar-persona")

fun CompleteAvatarUploadCommand.toCompleteUploadRequest() =
    CompleteUploadRequest(
        fileId,
    )

fun User.toUserWithAvatar(avatar: UserAvatar?) =
    UserWithAvatar(
        id, username, displayName, avatar?.url, createdAt, updatedAt
    )