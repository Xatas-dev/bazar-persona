package org.bazar.persona.domain.exceptions

import java.util.UUID

class UserAvatarNotFoundException(fileId: UUID)
    : DomainException("User avatar not found by fileId: $fileId")