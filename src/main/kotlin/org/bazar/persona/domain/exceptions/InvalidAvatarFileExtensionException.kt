package org.bazar.persona.domain.exceptions

class InvalidAvatarFileExtensionException(val fileExtension: String)
    : DomainException("$fileExtension is not allowed")