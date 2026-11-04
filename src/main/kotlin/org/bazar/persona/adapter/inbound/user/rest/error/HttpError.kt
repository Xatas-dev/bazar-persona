package org.bazar.persona.adapter.inbound.user.rest.error

import org.springframework.http.HttpStatusCode

data class HttpError(
    val status: HttpStatusCode,
    val body: ApiErrorResponse
)