package org.bazar.persona.adapter.inbound.user.rest

import org.bazar.persona.api.BazarUsersApi
import org.bazar.persona.application.user.service.GetUsersService
import org.bazar.persona.application.user_avatar.service.IAMAvatarPostService
import org.bazar.persona.application.user.service.IAMGetUserService
import org.bazar.persona.application.user.service.IAMPatchUserService
import org.bazar.persona.model.AvatarUploadRequestDto
import org.bazar.persona.model.AvatarUploadTicketDtoResponse
import org.bazar.persona.model.GetUsersDtoRequest
import org.bazar.persona.model.UpdateProfileDtoRequest
import org.bazar.persona.model.UpdateProfileDtoRequestV2
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.model.UserDtoResponseV2
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class UserController(
    private val iamGetUserService: IAMGetUserService,
    private val getUsersService: GetUsersService,
    private val iamPatchUserService: IAMPatchUserService,
    private val iamAvatarPostService: IAMAvatarPostService
) : BazarUsersApi {

    override fun usersGet(requestDto: GetUsersDtoRequest?): ResponseEntity<List<UserDtoResponse>> {
        return getUsersService.getUsersInfo(requestDto).toResponseEntity()
    }

    override fun usersIamGet(): ResponseEntity<UserDtoResponse> {
        return iamGetUserService.execute().toResponseEntity()
    }

    override fun usersIamPatch(updateProfileDtoRequest: UpdateProfileDtoRequest): ResponseEntity<UserDtoResponse> {
        return iamPatchUserService.updateUserInfo(updateProfileDtoRequest).toResponseEntity()
    }

    override fun v1UsersIamAvatarPost(avatarUploadRequestDto: AvatarUploadRequestDto): ResponseEntity<AvatarUploadTicketDtoResponse> {
        val uploadUrl = iamAvatarPostService.execute(avatarUploadRequestDto.toAvatarUpdateCommand())
        return ResponseEntity.ok(AvatarUploadTicketDtoResponse(URI(uploadUrl)))
    }

    override fun v2UsersGet(requestDto: GetUsersDtoRequest?): ResponseEntity<List<UserDtoResponseV2>> {
        return getUsersService.getUsersInfoV2(requestDto).toResponseEntityV2()
    }

    override fun v2UsersIamGet(): ResponseEntity<UserDtoResponseV2> {
        return iamGetUserService.executeV2().toResponseEntity()
    }

    override fun v2UsersIamPatch(updateProfileDtoRequestV2: UpdateProfileDtoRequestV2): ResponseEntity<UserDtoResponseV2> {
        return iamPatchUserService.updateUserInfoV2(updateProfileDtoRequestV2).toResponseEntity()
    }


}