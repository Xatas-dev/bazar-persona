package org.bazar.persona.adapter.outbound.files

import feign.QueryMap
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "bazar-files", url = $$"${http.bazar-files-url}")
interface BazarFilesFeignClient {

    @GetMapping("/v1/files/initiate-upload")
    fun initiateUpload(
        @SpringQueryMap request: InitiateUploadRequestDtoV1
    ): InitiateUploadResponseDtoV1

    @GetMapping("/v1/files/initiate-download")
    fun initiateDownload(@RequestParam fileUuid: String?): InitiateDownloadResponseV1Dto
}