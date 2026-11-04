package org.bazar.persona.application.user_avatar.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.persona.application.shared.port.out.CompleteUploadRequest
import org.bazar.persona.application.shared.port.out.FileStoragePort
import org.bazar.persona.application.user.service.command.AvatarUploadStatus
import org.bazar.persona.application.user.service.command.AvatarUploadValidationError
import org.bazar.persona.application.user.service.command.CompleteAvatarUploadCommand
import org.bazar.persona.application.user_avatar.port.out.UserAvatarRepositoryPort
import org.bazar.persona.domain.exceptions.UserAvatarNotFoundException
import org.bazar.persona.domain.user_avatar.UserAvatar
import org.bazar.persona.domain.user_avatar.UserAvatarStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CompleteAvatarUploadService(
    private val fileStoragePort: FileStoragePort,
    private val userAvatarRepositoryPort: UserAvatarRepositoryPort
) {

    private val logger = KotlinLogging.logger {  }

    @Transactional
    fun execute(command: CompleteAvatarUploadCommand) {
        logger.debug { "File uploaded command received: $command" }
        val existingAvatar = userAvatarRepositoryPort.findAvatarByFileId(command.fileId)
            ?: throw UserAvatarNotFoundException(command.fileId)

        if (existingAvatar.status != UserAvatarStatus.PENDING) {
            return
        }

        val updatedAvatar = when (command.status) {
            AvatarUploadStatus.UPLOADED -> onSuccess(existingAvatar)
            else -> onFail(existingAvatar, command.validationErrors)
        }

        userAvatarRepositoryPort.update(updatedAvatar)
    }

    private fun onSuccess(avatar: UserAvatar): UserAvatar {
        logger.info { "User avatar uploaded successfully, fileId: ${avatar.fileId}, user: ${avatar.userId}" }
        val prevActiveAvatar = userAvatarRepositoryPort.findActiveAvatarByUserId(true , avatar.userId)
        val downloadLinkDto = fileStoragePort.getDownloadLink(CompleteUploadRequest(avatar.fileId))

        if (prevActiveAvatar != null) {
            userAvatarRepositoryPort.update(prevActiveAvatar.setActive(false))
        }

        return avatar.uploadSuccess(downloadLinkDto.url)
    }

    private fun onFail(avatar: UserAvatar, errors: List<AvatarUploadValidationError>): UserAvatar {
        logger.warn { "User avatar upload failed, fileId: ${avatar.fileId}, errors: $errors" }
        return avatar.uploadFailed(errors.map { "${it.code}: ${it.description}" }.toString())
    }

}