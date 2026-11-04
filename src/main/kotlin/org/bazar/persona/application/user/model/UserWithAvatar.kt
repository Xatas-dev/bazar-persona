package org.bazar.persona.application.user.model

import java.time.Instant
import java.util.UUID

data class UserWithAvatar(
    val id: UUID,
    val username: String,
    val displayName: String,
    val avatarUrl: String?,
    val createdAt: Instant,
    var updatedAt: Instant
)
