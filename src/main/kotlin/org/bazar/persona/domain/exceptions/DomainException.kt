package org.bazar.persona.domain.exceptions

import org.springframework.http.HttpStatus


class DomainException(
    val exceptionType: DomainExceptions,
    customMessage: String = ""
) : RuntimeException(exceptionType.message + customMessage) {

    val errorMessage = exceptionType.message + customMessage
}

enum class DomainExceptions(
    val message: String,
    val httpStatus: HttpStatus
) {
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND)
}



