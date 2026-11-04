package org.bazar.persona.adapter.outbound.user_avatar

import org.bazar.persona.application.user_avatar.port.out.UserAvatarRepositoryPort
import org.bazar.persona.domain.user_avatar.UserAvatar
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserAvatarRepositoryAdapter(
    private val userAvatarCrudRepository: UserAvatarCrudRepository,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate
): UserAvatarRepositoryPort {
    override fun insert(avatar: UserAvatar): UserAvatar {
        return jdbcAggregateTemplate.insert(avatar.toUserAvatarEntity()).toUserAvatar()
    }

    override fun update(avatar: UserAvatar): UserAvatar {
        return jdbcAggregateTemplate.update(avatar.toUserAvatarEntity()).toUserAvatar()
    }

    override fun findActiveAvatarByUserId(isActive: Boolean, userId: UUID): UserAvatar? {
        return userAvatarCrudRepository.findByIsActiveAndUserId(isActive, userId)?.toUserAvatar()
    }

    override fun findAvatarByFileId(fileId: UUID): UserAvatar? {
        return userAvatarCrudRepository.findByFileId(fileId)?.toUserAvatar()
    }

}