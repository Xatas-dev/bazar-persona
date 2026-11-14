package org.bazar.persona.adapter.outbound.files

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "bazar-files", url = $$"${http.bazar-files-url}")
interface BazarFilesFeignClient {

    @PostMapping("/v1/files/initiate-upload")
    fun initiateUpload(
        @SpringQueryMap request: InitiateUploadRequestDtoV1
    ): InitiateUploadResponseDtoV1

    @GetMapping("/v1/files/{fileUuid}/public-url")
    fun initiateDownload(@PathVariable fileUuid: String): InitiateDownloadResponseV1Dto
}