package org.bazar.persona.application.user.service

import org.bazar.persona.application.shared.port.out.CompleteUploadRequest
import org.bazar.persona.application.shared.port.out.GetUploadUrlRequest
import org.bazar.persona.application.user.service.command.CompleteAvatarUploadCommand
import org.bazar.persona.application.user.service.command.InitiateAvatarUploadCommand
import java.util.UUID

fun InitiateAvatarUploadCommand.toGetUploadUrlRequest(userId: UUID) =
    GetUploadUrlRequest(userId, fileName, sizeBytes, contentType, "bazar-persona")

fun CompleteAvatarUploadCommand.toCompleteUploadRequest() =
    CompleteUploadRequest(
        fileId,
    )
