package org.bazar.persona.application.user.port.out

import org.bazar.persona.domain.user.User
import org.springframework.stereotype.Repository
import java.util.UUID

interface UserRepositoryPort{
    fun findByIdIn(ids: List<UUID>): List<User>

    fun findFirst10ByUserNameContainsIgnoreCase(userName: String): List<User>

    fun findById(id: UUID): User?

    fun insert(user: User): User

    fun update(user: User): User
}