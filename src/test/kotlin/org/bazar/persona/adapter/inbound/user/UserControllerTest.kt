package org.bazar.persona.adapter.inbound.user

import org.assertj.core.api.Assertions.assertThat
import org.bazar.persona.application.shared.port.out.CurrentUserProvider
import org.bazar.persona.application.shared.port.out.FileStoragePort
import org.bazar.persona.application.shared.port.out.FileUploadLinkDto
import org.bazar.persona.application.user.port.out.UserRepositoryPort
import org.bazar.persona.application.user_avatar.port.out.UserAvatarRepositoryPort
import org.bazar.persona.config.BaseWebTest
import org.bazar.persona.domain.user.User
import org.bazar.persona.domain.user_avatar.UserAvatar
import org.bazar.persona.model.AvatarUploadRequestDto
import org.bazar.persona.model.AvatarUploadTicketDtoResponse
import org.bazar.persona.model.UpdateProfileDtoRequestV2
import org.bazar.persona.model.UserDtoResponseV2
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import java.util.*
import javax.sql.DataSource

class UserControllerTest : BaseWebTest() {

    @MockitoBean
    private lateinit var currentUserProvider: CurrentUserProvider

    @Autowired
    private lateinit var userRepositoryPort: UserRepositoryPort

    @Autowired
    private lateinit var userAvatarRepositoryPort: UserAvatarRepositoryPort

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var filesStoragePort: FileStoragePort

    @Test
    @DisplayName("GET /v2/users/iam returns the current IAM user (happy path)")
    fun getIamV2_happyPath() {
        //given
        val newUserId = UUID.randomUUID()
        val newUserUsername = "PAPA JOHN'S"
        val avatarUrl = "https://test.s3.download/123"
        whenever(currentUserProvider.id).thenReturn(newUserId)
        whenever(currentUserProvider.username).thenReturn(newUserUsername)

        //when

        val response = mockMvc.get("/v2/users/iam")
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString
        val responseBody = objectMapper.readValue(response, UserDtoResponseV2::class.java)

        //then
        assertThat(responseBody.id).isEqualTo(newUserId)
        assertThat(responseBody.username).isEqualTo(newUserUsername)
        assertThat(responseBody.displayName).isEqualTo(newUserUsername)

        //post avatar load check
        userAvatarRepositoryPort.insert(
            UserAvatar.createAvatarForUser(newUserId, UUID.randomUUID()).uploadSuccess(avatarUrl)
        )
        assertThat(responseBody.avatarUrl).isNull()
    }

    @Test
    @DisplayName("GET /v2/users returns users by ids (happy path)")
    fun getUsersV2_byIds_happyPath() {
        //given
        val firstId = UUID.randomUUID()
        val secondId = UUID.randomUUID()
        val thirdId = UUID.randomUUID()
        userRepositoryPort.insert(User(firstId, "papa_johns", "PAPA JOHN'S"))
        userRepositoryPort.insert(User(secondId, "galya_nosex", "GALYA nosex"))
        userRepositoryPort.insert(User(thirdId, "random_user", "Random User"))
        val avatarUrl1 = "https://test.s3.download/123"
        val avatarUrl2 = "https://test.s3.download/1234"
        val avatarUrl3 = "https://test.s3.download/12345"
        userAvatarRepositoryPort.insert(
            UserAvatar.createAvatarForUser(firstId, UUID.randomUUID()).uploadSuccess(avatarUrl1)
        )
        userAvatarRepositoryPort.insert(
            UserAvatar.createAvatarForUser(secondId, UUID.randomUUID()).uploadSuccess(avatarUrl2)
        )
        userAvatarRepositoryPort.insert(
            UserAvatar.createAvatarForUser(thirdId, UUID.randomUUID()).uploadSuccess(avatarUrl3)
        )
        //when
        val response = mockMvc.get("/v2/users") {
            param("ids", firstId.toString(), secondId.toString())
        }
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

        val responseBody = objectMapper.readValue(response, Array<UserDtoResponseV2>::class.java).toList()

        //then
        assertThat(responseBody).hasSize(2)
        val usersById = responseBody.associateBy { it.id }
        assertThat(usersById[firstId]?.username).isEqualTo("papa_johns")
        assertThat(usersById[firstId]?.displayName).isEqualTo("PAPA JOHN'S")
        assertThat(usersById[firstId]?.avatarUrl.toString()).isEqualTo(avatarUrl1)
        assertThat(usersById[secondId]?.username).isEqualTo("galya_nosex")
        assertThat(usersById[secondId]?.displayName).isEqualTo("GALYA nosex")
        assertThat(usersById[secondId]?.avatarUrl.toString()).isEqualTo(avatarUrl2)
    }

    @Test
    @DisplayName("GET /v2/users returns users matching the search string (happy path)")
    fun getUsersV2_bySearch_happyPath() {
        //given
        userRepositoryPort.insert(User(UUID.randomUUID(), "papa_johns", "PAPA JOHN'S"))
        userRepositoryPort.insert(User(UUID.randomUUID(), "galya_nosex", "GALYA nosex"))
        userRepositoryPort.insert(User(UUID.randomUUID(), "papa_random", "Papa Random"))

        //when
        val response = mockMvc.get("/v2/users") {
            param("search", "papa")
        }
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

        val responseBody = objectMapper.readValue(response, Array<UserDtoResponseV2>::class.java).toList()

        //then
        assertThat(responseBody).hasSize(2)
        val usersByUsername = responseBody.associateBy { it.username }
        assertThat(usersByUsername["papa_johns"]?.displayName).isEqualTo("PAPA JOHN'S")
        assertThat(usersByUsername["papa_random"]?.displayName).isEqualTo("Papa Random")
    }

    @Test
    @DisplayName("GET /v2/users without filters returns an empty list")
    fun getUsersV2_noFilters_returnsEmpty() {
        //when
        val response = mockMvc.get("/v2/users")
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

        //then
        val responseBody = objectMapper.readValue(response, Array<UserDtoResponseV2>::class.java).toList()
        assertThat(responseBody).isEmpty()
    }

    @Test
    @DisplayName("GET /v2/users with a search that matches nothing returns an empty list")
    fun getUsersV2_searchNoMatch_returnsEmpty() {
        //given
        userRepositoryPort.insert(User(UUID.randomUUID(), "papa_johns", "PAPA JOHN'S"))

        //when
        val response = mockMvc.get("/v2/users") {
            param("search", "zzz")
        }
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

        //then
        val responseBody = objectMapper.readValue(response, Array<UserDtoResponseV2>::class.java).toList()
        assertThat(responseBody).isEmpty()
    }

    @Test
    @DisplayName("GET /v2/users with a too-short search returns 400")
    fun getUsersV2_searchTooShort_badRequest() {
        //when
        mockMvc.get("/v2/users") {
            param("search", "ab")
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    @DisplayName("PATCH /v2/users/iam updates the current user's display name (happy path)")
    fun patchIamV2_happyPath() {
        //given
        val userId = UUID.randomUUID()
        val newDisplayName = "GALYA nosex"

        val savedUser = userRepositoryPort.insert(User(userId, "rand", "rand"))
        val avatarUrl = "https://test.s3.download/123"
        userAvatarRepositoryPort.insert(
            UserAvatar.createAvatarForUser(userId, UUID.randomUUID()).uploadSuccess(avatarUrl)
        )

        whenever(currentUserProvider.id).thenReturn(userId)

        val request = UpdateProfileDtoRequestV2(displayName = newDisplayName)
        //when
        val response = mockMvc.patch("/v2/users/iam") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

        val responseBody = objectMapper.readValue(response, UserDtoResponseV2::class.java)
        assertThat(responseBody.id).isEqualTo(userId)
        assertThat(responseBody.displayName).isEqualTo(newDisplayName)
        assertThat(responseBody.avatarUrl.toString()).isEqualTo(avatarUrl)
    }

    @Test
    fun postAvatarIam_happyPath() {
        //given
        val userId = UUID.randomUUID()
        val fileId = UUID.randomUUID()
        val uploadUrl = "https://s3.test/$fileId"
        val savedUser = userRepositoryPort.insert(User(userId, "rand", "rand"))
        val postAvatarRequest = AvatarUploadRequestDto("PORN.png", "image/png", 1234L)
        whenever(currentUserProvider.id).thenReturn(userId)
        whenever(filesStoragePort.initiateUpload(any()))
            .thenReturn(FileUploadLinkDto(uploadUrl, fileId))
        //when


        val response = mockMvc.post("/v1/users/iam/avatar") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(postAvatarRequest)
        }
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString
        val responseBody = objectMapper.readValue(response, AvatarUploadTicketDtoResponse::class.java)

        assertThat(responseBody.uploadUrl.toString()).isEqualTo(uploadUrl)

    }

}