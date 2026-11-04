package org.bazar.persona.adapter.inbound.user.kafka

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.awaitility.kotlin.withPollInterval
import org.bazar.persona.application.shared.port.out.FileDownloadLinkDto
import org.bazar.persona.application.shared.port.out.FileStoragePort
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.application.user_avatar.port.out.UserAvatarRepositoryPort
import org.bazar.persona.config.BaseIntegrationTest
import org.bazar.persona.domain.user.User
import org.bazar.persona.domain.user_avatar.UserAvatar
import org.bazar.persona.domain.user_avatar.UserAvatarStatus
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.bean.override.mockito.MockitoBean
import tools.jackson.databind.ObjectMapper
import java.time.Duration
import java.util.*

class BazarFilesEventConsumerTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepositoryPort: UserRepositoryPort

    @Autowired
    private lateinit var userAvatarRepositoryPort: UserAvatarRepositoryPort

    @MockitoBean
    private lateinit var fileStoragePort: FileStoragePort

    @Value($$"${spring.kafka.topics.bazar-files-events}")
    private lateinit var filesEventTopic: String

    private fun insertUser(userId: UUID) {
        userRepositoryPort.insert(User(userId, "pens", "pens"))
    }

    private fun insertPendingAvatar(userId: UUID, fileId: UUID) {
        userAvatarRepositoryPort.insert(UserAvatar.createAvatarForUser(userId, fileId))
    }

    private fun uploadedEvent(fileId: UUID) = BazarFilesEvent(
        domain = "bazar-persona",
        size = 1234L,
        fileUuid = fileId,
        status = FileUploadStatus.UPLOADED,
        fileName = "avatar.png",
        contentType = "image/png",
        errors = emptyList()
    )

    private fun failedEvent(fileId: UUID, errors: List<FileUploadError>) =
        BazarFilesEvent(
            domain = "bazar-persona",
            size = 1234L,
            fileUuid = fileId,
            status = FileUploadStatus.ERROR,
            fileName = "avatar.png",
            contentType = "image/png",
            errors = errors
        )

    private fun send(event: BazarFilesEvent) {
        kafkaTemplate.send(filesEventTopic, objectMapper.writeValueAsString(event)).get()
    }

    private fun awaitAvatar(userId: UUID, block: (UserAvatar?) -> Unit) {
        await atMost Duration.ofSeconds(30) withPollInterval Duration.ofMillis(500) untilAsserted {
            block(userAvatarRepositoryPort.findActiveAvatarByUserId(true, userId))
        }
    }

    @Test
    fun consumeFileEvent_happyPath() {
        val userId = UUID.randomUUID()
        val fileId = UUID.randomUUID()
        val expectedUrl = "https://s3.bazar/$fileId"

        insertUser(userId)
        insertPendingAvatar(userId, fileId)

        whenever(fileStoragePort.getDownloadLink(any()))
            .thenReturn(FileDownloadLinkDto(expectedUrl))

        send(uploadedEvent(fileId))

        awaitAvatar(userId) { avatar ->
            assertThat(avatar).isNotNull
            assertThat(avatar!!.status).isEqualTo(UserAvatarStatus.SUCCESS)
            assertThat(avatar.url).isEqualTo(expectedUrl)
            assertThat(avatar.isActive).isTrue()
        }
    }

    @Test
    fun consumeFileEvent_ignoresEventForOtherDomain() {
        val userId = UUID.randomUUID()
        val fileId = UUID.randomUUID()

        insertUser(userId)
        insertPendingAvatar(userId, fileId)

        val event = uploadedEvent(fileId).copy(domain = "some-other-domain")
        send(event)

        Thread.sleep(3_000)

        verify(fileStoragePort, never()).getDownloadLink(any())

        val stored = userAvatarRepositoryPort.findAvatarByFileId(fileId)
        assertThat(stored).isNotNull
        assertThat(stored!!.status).isEqualTo(UserAvatarStatus.PENDING) // adjust to your enum
        assertThat(stored.isActive).isFalse()
    }

    @Test
    fun consumeFileEvent_unknownFileId_doesNotPoisonConsumer() {
        val unknownFileId = UUID.randomUUID()
        send(uploadedEvent(unknownFileId))

        val userId = UUID.randomUUID()
        val fileId = UUID.randomUUID()
        val url = "https://s3.bazar/$fileId"

        insertUser(userId)
        insertPendingAvatar(userId, fileId)
        whenever(fileStoragePort.getDownloadLink(any())).thenReturn(FileDownloadLinkDto(url))

        send(uploadedEvent(fileId))

        awaitAvatar(userId) { avatar ->
            assertThat(avatar).isNotNull
            assertThat(avatar!!.status).isEqualTo(UserAvatarStatus.SUCCESS)
        }
    }

    @Test
    fun consumeFileEvent_uploadFailed_marksAvatarFailed() {
        val userId = UUID.randomUUID()
        val fileId = UUID.randomUUID()

        insertUser(userId)
        insertPendingAvatar(userId, fileId)

        send(failedEvent(fileId, listOf(FileUploadError("INVALID_EXTENSION", "Not supported file extension"))))

        await atMost Duration.ofSeconds(30) withPollInterval Duration.ofMillis(500) untilAsserted {
            val avatar = userAvatarRepositoryPort.findAvatarByFileId(fileId)
            assertThat(avatar).isNotNull
            assertThat(avatar!!.status).isNotEqualTo(UserAvatarStatus.ERROR)
        }

        val stored = userAvatarRepositoryPort.findAvatarByFileId(fileId)!!
        assertThat(stored.status).isEqualTo(UserAvatarStatus.ERROR)
        assertThat(stored.isActive).isFalse()
        verify(fileStoragePort, never()).getDownloadLink(any())
    }

}