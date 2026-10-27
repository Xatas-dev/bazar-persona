package org.bazar.persona.adapter.outbound.user.pesistence

import org.bazar.persona.domain.user.User

fun UserEntity.toUser(): User {
    return User(
        id!!,
        username,
        displayName,
        createdAt,
        updatedAt
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id,
        username,
        displayName,
        createdAt,
        updatedAt
    )
}