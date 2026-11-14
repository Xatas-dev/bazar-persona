package org.bazar.persona.application.shared.port.out

import java.net.URL
import java.util.UUID

data class FileUploadLinkDto (
    val uploadUrl: String,
    val fileUuid: UUID
)