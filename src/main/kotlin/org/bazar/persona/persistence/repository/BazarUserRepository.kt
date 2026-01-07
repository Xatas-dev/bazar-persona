package org.bazar.persona.persistence.repository

import org.bazar.persona.persistence.entity.BazarUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BazarUserRepository : JpaRepository<BazarUser, UUID>{
    fun findByIdIn(ids: List<UUID>): List<BazarUser>

    fun findFirst10ByUserNameContainsIgnoreCase(userName: String): List<BazarUser>
}