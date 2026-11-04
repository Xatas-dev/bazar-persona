package org.bazar.persona.domain.exceptions

import java.util.UUID

class UserNotFoundException(userId: UUID)
    :DomainException("User $userId not found")