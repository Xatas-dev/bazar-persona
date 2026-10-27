package org.bazar.persona.adapter.outbound.user.pesistence.repository

import org.bazar.persona.adapter.outbound.user.pesistence.toUser
import org.bazar.persona.adapter.outbound.user.pesistence.toUserEntity
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.domain.user.User
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Repository
@Transactional
class UserRepositorySpringAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
):  UserRepositoryPort {
    override fun findByIdIn(ids: List<UUID>): List<User> {
        return userJpaRepository.findByIdIn(ids).map { it.toUser() }
    }

    override fun findFirst10ByUserNameContainsIgnoreCase(userName: String): List<User> {
        return userJpaRepository.findFirst10ByUsernameContainsIgnoreCase(userName).map { it.toUser() }
    }

    override fun findById(id: UUID): User? {
        return userJpaRepository.findById(id).getOrNull()?.toUser()
    }

    override fun insert(user: User): User {
        val entity = user.toUserEntity()
        return jdbcAggregateTemplate.insert(entity).toUser()
    }

    override fun update(user: User): User {
        return jdbcAggregateTemplate.update(user.toUserEntity()).toUser()
    }
}