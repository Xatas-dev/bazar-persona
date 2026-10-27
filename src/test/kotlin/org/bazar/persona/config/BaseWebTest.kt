package org.bazar.persona.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
class BaseWebTest : BaseIntegrationTest() {
    @Autowired
    lateinit var mockMvc: MockMvc
}