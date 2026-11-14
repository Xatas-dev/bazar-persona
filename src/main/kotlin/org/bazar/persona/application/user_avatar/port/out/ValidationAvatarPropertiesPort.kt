package org.bazar.persona.application.user_avatar.port.out

interface ValidationAvatarPropertiesPort {

    fun getAllowedFormat(): List<String>

    fun getMaxSizeBytes(): Long

    fun getAllowedMimeTypes(): List<String>

}