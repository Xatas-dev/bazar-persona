package org.bazar.persona.adapter.outbound.user.pesistence.repository

import org.bazar.persona.adapter.outbound.user.pesistence.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
interface UserJpaRepository : CrudRepository<UserEntity, UUID>{
    fun findByIdIn(ids: List<UUID>): List<UserEntity>

    fun findFirst10ByUsernameContainsIgnoreCase(userName: String): List<UserEntity>
}