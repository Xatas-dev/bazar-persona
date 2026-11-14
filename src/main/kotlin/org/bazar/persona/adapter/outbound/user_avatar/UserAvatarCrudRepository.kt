package org.bazar.persona.adapter.outbound.user_avatar

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserAvatarCrudRepository: CrudRepository<UserAvatarEntity, Long> {
    fun findByIsActiveAndUserId(isActive: Boolean, userId: UUID): UserAvatarEntity?

    fun findByIsActiveAndUserIdIn(isActive: Boolean, userIds: List<UUID>): List<UserAvatarEntity>

    fun findByFileId(fileId: UUID): UserAvatarEntity?
}