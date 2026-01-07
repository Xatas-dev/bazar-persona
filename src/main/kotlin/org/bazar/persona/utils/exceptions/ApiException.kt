package org.bazar.persona.utils.exceptions

import org.springframework.http.HttpStatus


class ApiException(
    val exceptionType: ApiExceptions,
    customMessage: String = ""
) : RuntimeException(exceptionType.message + customMessage) {

    val errorMessage = exceptionType.message + customMessage
}

enum class ApiExceptions(
    val message: String,
    val httpStatus: HttpStatus
) {
    USER_NOT_FOUND("user not found", HttpStatus.NOT_FOUND)
}



