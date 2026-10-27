package org.bazar.persona.application.shared.port.out

import java.util.UUID

interface CurrentUserProvider {
    val id: UUID
    val username: String
    val accessToken: String
}
