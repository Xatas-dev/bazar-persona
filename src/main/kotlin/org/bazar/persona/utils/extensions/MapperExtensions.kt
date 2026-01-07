package org.bazar.persona.utils.extensions

import org.bazar.persona.model.UpdateProfileDtoRequest
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.persistence.entity.BazarUser
import java.time.Instant

fun BazarUser.toUserDtoResponse() =
    UserDtoResponse(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userName = userName,
        email = email,
        firstName = firstName,
        lastName = lastName,
        userPic = userPic
    )

fun BazarUser.updateFrom(dto : UpdateProfileDtoRequest): BazarUser {
    val updated = false
    userName = dto.userName ?: userName
    firstName = dto.firstName ?: firstName
    lastName = dto.lastName ?: lastName


    if (updated)
        updatedAt = Instant.now()
    return this
}