package org.bazar.persona.infra.config.properties

import org.bazar.persona.application.user_avatar.port.out.ValidationAvatarPropertiesPort
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "validation")
data class ValidationProperties(
    val avatar: AvatarProperties
): ValidationAvatarPropertiesPort {
    override fun getAllowedFormat(): List<String> {
        return avatar.allowedFormats
    }

    override fun getMaxSizeBytes(): Long {
        return avatar.maxSizeBytes
    }

    override fun getAllowedMimeTypes(): List<String> {
        return avatar.allowedMimeTypes
    }

    data class AvatarProperties(
        val allowedFormats: List<String> = emptyList(),
        val maxSizeBytes: Long = 5_000_000,
        val allowedMimeTypes: List<String> = emptyList()
    )
}