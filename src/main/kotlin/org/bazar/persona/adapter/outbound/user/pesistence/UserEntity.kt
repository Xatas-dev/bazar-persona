package org.bazar.persona.adapter.outbound.user.pesistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table(name = "bazar_user")
data class UserEntity(
    @Id
    val id: UUID? = null,

    val username: String,

    var displayName: String,

    var createdAt: Instant = Instant.now(),

    var updatedAt: Instant = Instant.now()
)