package org.bazar.persona.domain.user

import java.time.Instant
import java.util.*

data class User(
    val id: UUID,
    val username: String,
    var displayName: String,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
) {

    fun update(displayName: String): User {
        return copy(displayName = displayName, updatedAt = Instant.now())
    }

}