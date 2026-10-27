package org.bazar.persona.adapter.inbound.user

import org.bazar.persona.domain.user.User
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.model.UserDtoResponseV2
import org.springframework.http.ResponseEntity

fun User.toUserDtoResponse() =
    UserDtoResponse(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userName = username
    )

fun User.toUserDtoResponseV2() =
    UserDtoResponseV2(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        username = username,
        displayName = displayName
    )

fun UserDtoResponse.toResponseEntity(): ResponseEntity<UserDtoResponse> {
    return ResponseEntity.ok(this)
}

fun UserDtoResponseV2.toResponseEntity(): ResponseEntity<UserDtoResponseV2> {
    return ResponseEntity.ok(this)
}

fun List<UserDtoResponse>.toResponseEntity() =
    ResponseEntity.ok(this)

fun List<UserDtoResponseV2>.toResponseEntityV2() =
    ResponseEntity.ok(this)