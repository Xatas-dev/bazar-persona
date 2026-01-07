package org.bazar.persona.utils.extensions

import org.bazar.persona.model.UserDtoResponse
import org.springframework.http.ResponseEntity

fun UserDtoResponse.toResponseEntity(): ResponseEntity<UserDtoResponse> {
    return ResponseEntity.ok(this)
}

fun List<UserDtoResponse>.toResponseEntity() =
    ResponseEntity.ok(this)