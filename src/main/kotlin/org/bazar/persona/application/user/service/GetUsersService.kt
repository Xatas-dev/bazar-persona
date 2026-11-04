package org.bazar.persona.application.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.bazar.persona.application.user.model.UserWithAvatar
import org.bazar.persona.application.user.model.query.GetUsersQuery
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.application.user_avatar.port.out.UserAvatarRepositoryPort
import org.springframework.stereotype.Service

@Service
class GetUsersService(
    private val userRepositoryPort: UserRepositoryPort,
    private val userAvatarRepositoryPort: UserAvatarRepositoryPort
) {

    private val logger = KotlinLogging.logger { }

    @Deprecated("Deprecated since 1.0.1, will be removed in 1.0.3")
    fun getUsersInfo(requestDto: GetUsersQuery): List<UserWithAvatar> {
        if (requestDto.userIds == null && requestDto.searchString == null)
            return emptyList()

        if (requestDto.userIds != null) {
            logger.info { "Get user request contain ids field, proceeding exact match by ids: ${requestDto.userIds}" }
            return userRepositoryPort.findByIdIn(requestDto.userIds).map { it.toUserWithAvatar(null) }
        }

        logger.info { "Get user request does not contain ids field, proceeding contains match by: ${requestDto.searchString}" }
        return userRepositoryPort.findFirst10ByUserNameContainsIgnoreCase(
            requestDto.searchString ?: ""
        )
            .map { it.toUserWithAvatar(null) }
    }

    fun getUsersInfoV2(requestDto: GetUsersQuery): List<UserWithAvatar> {
        if (requestDto.userIds == null && requestDto.searchString == null)
            return emptyList()


        val users = if (requestDto.userIds != null) {
            logger.info { "Get user request contain ids field, proceeding exact match by ids: ${requestDto.userIds}" }
            userRepositoryPort.findByIdIn(requestDto.userIds)
        } else {
            logger.info { "Get user request does not contain ids field, proceeding contains match by: ${requestDto.searchString}" }
            userRepositoryPort.findFirst10ByUserNameContainsIgnoreCase(
                requestDto.searchString ?: ""
            )
        }

        val avatarsByUserId =
            userAvatarRepositoryPort.findActiveAvatarByUserIds(users.map { it.id }).associateBy { it.userId }
        return users.map { it.toUserWithAvatar(avatarsByUserId[it.id]) }
    }

}