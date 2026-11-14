package org.bazar.persona.adapter.inbound.user.kafka

import org.bazar.persona.application.user_avatar.service.CompleteAvatarUploadService
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class BazarFilesEventConsumer(
    private val completeAvatarUploadService: CompleteAvatarUploadService
) {

    @Value($$"${file-domain}")
    private lateinit var fileDomain: String

    @KafkaListener(
        topics = [$$"${spring.kafka.topics.bazar-files-events}"]
    )
    fun consume(event: BazarFilesEvent) {
        if (event.domain != fileDomain)
            return

        completeAvatarUploadService.execute(event.toCompleteAvatarUploadCommand())
    }
}