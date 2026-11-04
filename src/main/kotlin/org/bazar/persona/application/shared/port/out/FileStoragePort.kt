package org.bazar.persona.application.shared.port.out

interface FileStoragePort {

    fun initiateUpload(request: GetUploadUrlRequest): FileUploadLinkDto

    fun getDownloadLink(request: CompleteUploadRequest): FileDownloadLinkDto

}