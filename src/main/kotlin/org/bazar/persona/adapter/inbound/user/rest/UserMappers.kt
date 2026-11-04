package org.bazar.persona.adapter.inbound.user.rest

import org.bazar.persona.application.user.model.UserWithAvatar
import org.bazar.persona.application.user.model.command.InitiateAvatarUploadCommand
import org.bazar.persona.application.user.model.query.GetUsersQuery
import org.bazar.persona.application.user.model.query.PatchUserProfileQuery
import org.bazar.persona.application.user.model.query.PatchUserProfileQueryV2
import org.bazar.persona.model.AvatarUploadRequestDto
import org.bazar.persona.model.GetUsersDtoRequest
import org.bazar.persona.model.UpdateProfileDtoRequest
import org.bazar.persona.model.UpdateProfileDtoRequestV2
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.model.UserDtoResponseV2
import org.bazar.persona.utils.extensions.toUri
import org.springframework.http.ResponseEntity

fun UserWithAvatar.toUserDtoResponse() =
    UserDtoResponse(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        userName = username,
        firstName = displayName,
        lastName = "",
        email = "rand@mail.com",
        userPic = ""
    )

fun UserWithAvatar.toUserDtoResponseV2() =
    UserDtoResponseV2(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        username = username,
        displayName = displayName,
        avatarUrl = avatarUrl?.toUri()
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

fun AvatarUploadRequestDto.toAvatarUpdateCommand() =
    InitiateAvatarUploadCommand(
        fileName,  contentType, sizeBytes
    )

fun GetUsersDtoRequest?.toGetUsersQuery() = GetUsersQuery(
    this?.ids, this?.search
)

fun UpdateProfileDtoRequestV2.toPatchUserProfileQueryV2() =
    PatchUserProfileQueryV2(displayName)

fun UpdateProfileDtoRequest.toPatchUserProfileQuery() =
    PatchUserProfileQuery(userName)