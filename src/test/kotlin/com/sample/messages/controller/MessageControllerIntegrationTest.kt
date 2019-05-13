package com.sample.messages.controller

import com.sample.messages.fixtures.MessageContainerServiceStub
import com.sample.messages.fixtures.MessageContentFixtures.emptyMessage
import com.sample.messages.fixtures.MessageContentFixtures.testMessage_1
import com.sample.messages.fixtures.MessageContentFixtures.testMessage_2
import com.sample.messages.fixtures.MessageContentFixtures.testMessage_3
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension::class)
@SpringBootTest
class MessageControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var messageContainerService: MessageContainerServiceStub

    @BeforeEach
    fun after() {
        messageContainerService.clear()
    }

    @Test
    fun `should add and retrieve all messages`() {
        getMessagesAndExpectBody(emptyMessage())

        postMessage(testMessage_1())

        postMessage(testMessage_2())

        postMessage(testMessage_3())

        webTestClient
                .get().uri("/messages")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json("""[{"message":"test message1"}, {"message":"test message2"}, {"message":"test message3"}]""")
    }

    @Test
    fun `should count all messages`() {
        getMessagesAndExpectBody(emptyMessage())

        postMessage(testMessage_1())

        postMessage(testMessage_2())

        postMessage(testMessage_3())

        webTestClient
                .get().uri("/messages/count")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json("""{ "count": 3 }""")
    }

    private fun getMessagesAndExpectBody(jsonBody: String) {
        webTestClient
                .get().uri("/messages")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(jsonBody)
    }

    private fun postMessage(messageContent: String) {
        webTestClient
                .post().uri("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(messageContent)
                .exchange()
                .expectStatus().isOk
    }
}
