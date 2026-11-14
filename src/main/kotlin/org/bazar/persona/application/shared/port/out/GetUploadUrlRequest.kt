package org.bazar.persona.application.shared.port.out

import java.util.UUID

data class GetUploadUrlRequest(
    val userId: UUID,
    val fileName: String,
    val size: Long,
    val contentType: String,
    val domain: String
)
