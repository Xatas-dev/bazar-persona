package org.bazar.persona.application.user.model.query

import java.util.UUID

data class GetUsersQuery(
    val userIds: List<UUID>?,
    val searchString: String?
)
