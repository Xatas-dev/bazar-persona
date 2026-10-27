package org.bazar.persona.infra.config.web

import org.bazar.persona.domain.exceptions.DomainException
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpaceRestControllerAdvice {

    @ExceptionHandler(DomainException::class)
    fun handleAccessDenied(ex: DomainException): ResponseEntity<ProblemDetail> {
        return ResponseEntity.status(ex.exceptionType.httpStatus)
            .body(ProblemDetail.forStatusAndDetail(ex.exceptionType.httpStatus, ex.errorMessage))
    }

}