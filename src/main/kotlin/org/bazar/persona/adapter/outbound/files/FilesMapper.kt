package org.bazar.persona.adapter.outbound.files

import org.bazar.persona.application.shared.port.out.FileUploadLinkDto
import org.bazar.persona.application.shared.port.out.GetUploadUrlRequest
import org.bazar.persona.utils.extensions.toUuid
import java.net.URI

fun GetUploadUrlRequest.toFeignRequest() = InitiateUploadRequestDtoV1(
    fileName, size, contentType, domain
)

fun InitiateUploadResponseDtoV1.toFileUploadLinkDto() =
    FileUploadLinkDto(
        uploadUrl, fileUuid.toUuid()
    )