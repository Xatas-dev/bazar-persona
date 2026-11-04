package org.bazar.persona.domain.exceptions


sealed class DomainException(
    techMessage: String,
    cause: Throwable? = null,
) : RuntimeException(techMessage, cause)
