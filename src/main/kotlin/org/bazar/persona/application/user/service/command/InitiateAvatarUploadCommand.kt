package org.bazar.persona.application.user.service.command

data class InitiateAvatarUploadCommand(
    val fileName: String,
    val contentType: String,
    val sizeBytes: Long
)
