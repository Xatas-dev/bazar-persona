package org.bazar.persona.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.time.Instant
import java.util.*

@Entity
@Table(name = "bazar_user")
class BazarUser(
    @Id
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "user_name", nullable = false, length = 128)
    var userName: String? = null,

    @Column(name = "user_pic", length = 256)
    var userPic: String? = null,

    @Column(name = "email", length = 128)
    var email: String? = null,

    @Column(name = "first_name", length = 128)
    var firstName: String? = null,

    @Column(name = "last_name", length = 128)
    var lastName: String? = null,

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now()
)