package org.bazar.persona.adapter.inbound.user.rest.error

import org.bazar.persona.domain.exceptions.DomainException
import org.bazar.persona.domain.exceptions.InvalidAvatarFileExtensionException
import org.bazar.persona.domain.exceptions.InvalidAvatarFileTypeException
import org.bazar.persona.domain.exceptions.InvalidAvatarSizeException
import org.bazar.persona.domain.exceptions.UserAvatarNotFoundException
import org.bazar.persona.domain.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus

fun DomainException.toHttpError(): HttpError = when (this) {
    is InvalidAvatarFileExtensionException ->
        httpError(HttpStatus.BAD_REQUEST, "INVALID_AVATAR_FILE_EXTENSION", "Неподдерживаемое расширение файла $fileExtension")

    is InvalidAvatarFileTypeException ->
        httpError(HttpStatus.BAD_REQUEST, "INVALID_AVATAR_FILE_TYPE", "Неподдерживаемый тип файла $fileType")

    is InvalidAvatarSizeException ->
        httpError(HttpStatus.BAD_REQUEST, "INVALID_AVATAR_SIZE", "Размер файла слишком большой")

    is UserAvatarNotFoundException ->
        httpError(HttpStatus.NOT_FOUND, "USER_AVATAR_NOT_FOUND", "Ошибка при обработке файла, попробуйте позже")

    is UserNotFoundException ->
        httpError(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "Пользователь не найден")
}

private fun httpError(status: HttpStatus, code: String, message: String?): HttpError =
    HttpError(
        status = status,
        body = ApiErrorResponse(
            code = code,
            message = message ?: code
        ),
    )