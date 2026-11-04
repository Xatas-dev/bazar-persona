package org.bazar.persona.application.user.model.command

data class InitiateAvatarUploadCommand(
    val fileName: String,
    val contentType: String,
    val sizeBytes: Long
)
