package org.bazar.persona.adapter.outbound.files

import org.bazar.persona.application.shared.port.out.*
import org.springframework.stereotype.Component

@Component
class BazarFilesStoragePort(
    private val bazarFilesFeignClient: BazarFilesFeignClient
) : FileStoragePort {

    override fun initiateUpload(request: GetUploadUrlRequest): FileUploadLinkDto {
        return bazarFilesFeignClient.initiateUpload(request.toFeignRequest()).toFileUploadLinkDto()
    }

    override fun getDownloadLink(request: CompleteUploadRequest): FileDownloadLinkDto {
        val response = bazarFilesFeignClient.initiateDownload(request.fileId.toString())

        return FileDownloadLinkDto(response.downloadUrl)
    }

}