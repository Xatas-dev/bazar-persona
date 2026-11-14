package org.bazar.persona.domain.exceptions

class InvalidAvatarSizeException(maxSize: Long, actualSize: Long)
    : DomainException("Avatar size $actualSize exceeds max size $maxSize")