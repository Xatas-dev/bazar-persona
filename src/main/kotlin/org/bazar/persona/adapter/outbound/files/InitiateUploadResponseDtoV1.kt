package org.bazar.persona.adapter.outbound.files

data class InitiateUploadResponseDtoV1(
    val uploadUrl: String,
    val fileUuid: String
)
