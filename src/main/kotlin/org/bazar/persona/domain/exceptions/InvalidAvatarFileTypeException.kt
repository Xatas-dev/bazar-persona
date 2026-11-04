package org.bazar.persona.domain.exceptions

class InvalidAvatarFileTypeException(val fileType: String)
    : DomainException("$fileType is not allowed")