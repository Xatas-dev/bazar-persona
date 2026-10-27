package org.bazar.persona.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.persona.adapter.inbound.user.toUserDtoResponse
import org.bazar.persona.adapter.inbound.user.toUserDtoResponseV2
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.model.GetUsersDtoRequest
import org.bazar.persona.model.UserDtoResponse
import org.bazar.persona.model.UserDtoResponseV2
import org.springframework.stereotype.Service

@Service
class GetUsersService(
    private val userRepositoryPort: UserRepositoryPort
) {

    private val logger = KotlinLogging.logger { }

    @Deprecated("Deprecated since 1.0.1, will be removed in 1.0.3")
    fun getUsersInfo(requestDto: GetUsersDtoRequest?): List<UserDtoResponse> {
        if (requestDto == null || (requestDto.ids == null && requestDto.search == null))
            return emptyList()

        if (requestDto.ids != null){
            logger.info { "Get user request contain ids field, proceeding exact match by ids: ${requestDto.ids}" }
            return userRepositoryPort.findByIdIn(requestDto.ids).map { it.toUserDtoResponse() }
        }

        logger.info { "Get user request does not contain ids field, proceeding contains match by: ${requestDto.search}" }
        return userRepositoryPort.findFirst10ByUserNameContainsIgnoreCase(
            requestDto.search ?: ""
        )
            .map { it.toUserDtoResponse() }
    }

    fun getUsersInfoV2(requestDto: GetUsersDtoRequest?): List<UserDtoResponseV2> {
        if (requestDto == null || (requestDto.ids == null && requestDto.search == null))
            return emptyList()

        if (requestDto.ids != null){
            logger.info { "Get user request contain ids field, proceeding exact match by ids: ${requestDto.ids}" }
            return userRepositoryPort.findByIdIn(requestDto.ids).map { it.toUserDtoResponseV2() }
        }

        logger.info { "Get user request does not contain ids field, proceeding contains match by: ${requestDto.search}" }
        return userRepositoryPort.findFirst10ByUserNameContainsIgnoreCase(
            requestDto.search ?: ""
        )
            .map { it.toUserDtoResponseV2() }
    }

}