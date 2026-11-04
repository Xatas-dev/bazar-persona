package org.bazar.persona.adapter.outbound.files

data class InitiateUploadRequestDtoV1(
    val fileName: String,
    val size: Long,
    val contentType: String,
    val domain: String
)
