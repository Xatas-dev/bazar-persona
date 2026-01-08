package org.bazar.persona.service

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.bazar.persona.model.GetUsersDtoRequest
import org.bazar.persona.model.UpdateProfileDtoRequest
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.persistence.entity.BazarUser
import org.bazar.persona.persistence.repository.BazarUserRepository
import org.bazar.persona.utils.exceptions.ApiException
import org.bazar.persona.utils.exceptions.ApiExceptions
import org.bazar.persona.utils.extensions.toUserDtoResponse
import org.bazar.persona.utils.extensions.toUuid
import org.bazar.persona.utils.extensions.updateFrom
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
@Transactional
class BazarUserService(
    private val bazarUserRepository: BazarUserRepository
) {

    private val logger = KotlinLogging.logger { }

    fun getAuthenticatedUserInfo(): UserDtoResponse {
        val userDetails = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val token = userDetails.token
        val userId = token.claims["sub"].toString()
        return bazarUserRepository.findById(userId.toUuid())
            .orElseGet {
                logger.info { "Haven't found user, saving new from jwt token, userId = $userId" }
                bazarUserRepository.save(
                    BazarUser(
                        id = userId.toUuid(),
                        userName = token.claims["preferred_username"]?.toString(),
                        email = token.claims["email"]?.toString(),
                        firstName = token.claims["given_name"]?.toString(),
                        lastName = token.claims["family_name"]?.toString()
                    )
                )
            }.toUserDtoResponse()
    }

    fun getUsersInfo(requestDto: GetUsersDtoRequest?): List<UserDtoResponse> {
        if (requestDto == null || (requestDto.ids == null && requestDto.search == null))
            return emptyList()

        if (requestDto.ids != null){
            logger.info { "Get user request contain ids field, proceeding exact match by ids: ${requestDto.ids}" }
            return bazarUserRepository.findByIdIn(requestDto.ids).map { it.toUserDtoResponse() }
        }

        logger.info { "Get user request does not contain ids field, proceeding contains match by: ${requestDto.search}" }
        return bazarUserRepository.findFirst10ByUserNameContainsIgnoreCase(
            requestDto.search ?: ""
        )
            .map { it.toUserDtoResponse() }
    }

    fun updateUserInfo(dto: UpdateProfileDtoRequest): UserDtoResponse {
        val userDetails = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val token = userDetails.token
        val userId = token.claims["sub"].toString()
        val user = bazarUserRepository.findById(userId.toUuid()).orElseThrow {
            logger.warn { "Tried to update user=$userId info, no such user found" }
            ApiException(ApiExceptions.USER_NOT_FOUND)
        }
        logger.info { "Successfully updated user info for user = $userId" }
        return user.updateFrom(dto).toUserDtoResponse()
    }

}