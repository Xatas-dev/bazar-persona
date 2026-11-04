package org.bazar.persona.application.shared.port.out

import java.util.UUID

data class CompleteUploadRequest(
    val fileId: UUID
)