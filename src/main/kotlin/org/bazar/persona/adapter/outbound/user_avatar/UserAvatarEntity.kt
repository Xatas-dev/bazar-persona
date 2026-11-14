package org.bazar.persona.adapter.outbound.user_avatar

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("user_avatar")
data class UserAvatarEntity(

    @Column("user_id")
    val userId: UUID,

    @Column("file_id")
    val fileId: UUID,

    @Column("is_active")
    var isActive: Boolean,

    @Column("status")
    var status: String,

    @Column("url")
    var url: String?,

    @Column("error_reason")
    var errorReason: String?,

    @Column("created_at")
    val createdAt: Instant,

    @Column("updated_at")
    var updatedAt: Instant,

    @Id
    @Column("id")
    val id: Long? = null
)