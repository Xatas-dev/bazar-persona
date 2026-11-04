package org.bazar.persona.application.user.model.command

import java.util.UUID

data class CompleteAvatarUploadCommand (
    val fileId: UUID,
    val status: AvatarUploadStatus,
    val validationErrors: List<AvatarUploadValidationError>
)

enum class AvatarUploadStatus {
    UPLOADED,
    ERROR,
    VALIDATION_ERROR,
    UNKNOWN;
}

data class AvatarUploadValidationError(
    val code: String,
    val description: String
)