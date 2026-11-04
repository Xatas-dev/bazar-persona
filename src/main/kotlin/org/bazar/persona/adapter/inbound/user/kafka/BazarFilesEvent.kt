package org.bazar.persona.adapter.inbound.user.kafka

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class BazarFilesEvent(
    val domain: String,
    val size: Long,
    val fileUuid: UUID,
    val status: FileUploadStatus,
    val fileName: String,
    val contentType: String,
    val errors: List<FileUploadError>
)

data class FileUploadError(
    val code: String,
    val description: String
)

enum class FileUploadStatus {
    UPLOADED,
    ERROR,
    VALIDATION_ERROR,
    UNKNOWN;

    @get:JsonValue
    val value: String
        get() = name

    companion object {
        @JsonCreator
        fun fromString(value: String): FileUploadStatus {
            return try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                UNKNOWN
            } catch (e: NullPointerException) {
                UNKNOWN
            }
        }
    }
}
