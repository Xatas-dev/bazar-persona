package org.bazar.persona.controller

import org.bazar.persona.api.BazarUsersApi
import org.bazar.persona.model.GetUsersDtoRequest
import org.bazar.persona.model.UpdateProfileDtoRequest
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.service.BazarUserService
import org.bazar.persona.utils.extensions.toResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BazarUserController(
    private val bazarUserService: BazarUserService
) : BazarUsersApi {

    override fun usersGet(requestDto: GetUsersDtoRequest?): ResponseEntity<List<UserDtoResponse>> {
        return bazarUserService.getUsersInfo(requestDto).toResponseEntity()
    }

    override fun usersIamGet(): ResponseEntity<UserDtoResponse> {
        return bazarUserService.getAuthenticatedUserInfo().toResponseEntity()
    }

    override fun usersIamPatch(updateProfileDtoRequest: UpdateProfileDtoRequest): ResponseEntity<UserDtoResponse> {
        return bazarUserService.updateUserInfo(updateProfileDtoRequest).toResponseEntity()
    }
}