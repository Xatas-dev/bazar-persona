package org.bazar.persona.config.web

import org.bazar.persona.utils.exceptions.ApiException
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SpaceRestControllerAdvice {

    @ExceptionHandler(ApiException::class)
    fun handleAccessDenied(ex: ApiException): ResponseEntity<ProblemDetail> {
        return ResponseEntity.status(ex.exceptionType.httpStatus)
            .body(ProblemDetail.forStatusAndDetail(ex.exceptionType.httpStatus, ex.errorMessage))
    }

}