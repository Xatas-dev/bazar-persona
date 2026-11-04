package org.bazar.persona.adapter.inbound.user.kafka

import org.bazar.persona.application.user.service.command.AvatarUploadStatus
import org.bazar.persona.application.user.service.command.AvatarUploadValidationError
import org.bazar.persona.application.user.service.command.CompleteAvatarUploadCommand

fun BazarFilesEvent.toCompleteAvatarUploadCommand() =
    CompleteAvatarUploadCommand(
        fileUuid,
        status.toAvatarUploadStatus(),
        errors.map { it.toAvatarUploadValidationError() }
    )


fun FileUploadStatus.toAvatarUploadStatus() =
    AvatarUploadStatus.valueOf(name)

fun FileUploadError.toAvatarUploadValidationError() =
    AvatarUploadValidationError(code, description)