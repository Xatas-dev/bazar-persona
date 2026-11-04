package org.bazar.persona.application.user_avatar.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.persona.application.shared.port.out.CurrentUserProvider
import org.bazar.persona.application.shared.port.out.FileStoragePort
import org.bazar.persona.application.user_avatar.port.out.ValidationAvatarPropertiesPort
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.application.user.service.command.InitiateAvatarUploadCommand
import org.bazar.persona.application.user.service.toGetUploadUrlRequest
import org.bazar.persona.application.user_avatar.port.out.UserAvatarRepositoryPort
import org.bazar.persona.domain.exceptions.InvalidAvatarFileExtensionException
import org.bazar.persona.domain.exceptions.InvalidAvatarFileTypeException
import org.bazar.persona.domain.exceptions.InvalidAvatarSizeException
import org.bazar.persona.domain.exceptions.UserNotFoundException
import org.bazar.persona.domain.user_avatar.UserAvatar.Companion.createAvatarForUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IAMAvatarPostService(
    private val validationProperties: ValidationAvatarPropertiesPort,
    private val fileStoragePort: FileStoragePort,
    private val userRepositoryPort: UserRepositoryPort,
    private val currentUserProvider: CurrentUserProvider,
    private val userAvatarRepositoryPort: UserAvatarRepositoryPort
) {

    private val logger = KotlinLogging.logger {  }

    @Transactional
    fun execute(command: InitiateAvatarUploadCommand): String {
        val currentUserId = currentUserProvider.id
        val user = userRepositoryPort.findById(currentUserId) ?: throw UserNotFoundException(currentUserId)

        validateCommand(command)

        val fileUploadData = fileStoragePort.initiateUpload(command.toGetUploadUrlRequest(currentUserId))
        userAvatarRepositoryPort.insert(createAvatarForUser(user.id, fileUploadData.fileUuid))
        logger.info { "Initiated avatar upload for user: $currentUserId, fileId: ${fileUploadData.fileUuid}" }
        return fileUploadData.uploadUrl
    }

    private fun validateCommand(command: InitiateAvatarUploadCommand) {
        val maxSize = validationProperties.getMaxSizeBytes()
        val allowedMimeTypes = validationProperties.getAllowedMimeTypes()
        val allowedFormats = validationProperties.getAllowedFormat()

        if (command.sizeBytes > maxSize) {
            throw InvalidAvatarSizeException(maxSize, command.sizeBytes)
        }

        if (command.contentType !in allowedMimeTypes) {
            throw InvalidAvatarFileTypeException(command.contentType)
        }

        val extension = command.fileName.substringAfterLast('.', "").lowercase()
        if (extension !in allowedFormats) {
            throw InvalidAvatarFileExtensionException(extension)
        }
    }

}