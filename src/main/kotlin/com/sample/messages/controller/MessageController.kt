package com.sample.messages.controller

import com.sample.messages.mapper.MessageMapper
import com.sample.messages.model.Message
import com.sample.messages.model.MessageView
import com.sample.messages.service.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@ObsoleteCoroutinesApi
@FlowPreview
@RestController
class MessageController(
        private val readMessageService: ReadMessageService,
        private val writeMessageService: WriteMessageService,
        private val timeService: TimeService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping("/messages")
    suspend fun addMessageToList(@RequestBody messageView: MessageView): Mono<ServerResponse> = coroutineScope {
        log.info { "Add message: $messageView" }
        writeMessageService.saveBy(AddMessageRead(MessageMapper.toMessage(messageView, timeService.nowDateTime())))
        ServerResponse.ok().build()
    }

    @GetMapping("/messages")
    suspend fun showMessages(): Flow<MessageView> = flow {
        val response = CompletableDeferred<List<Message>>()
        readMessageService.getBy(GetMessages(response))
        val messages = response.await()
        messages.forEach {
            emit(MessageMapper.toMessageView(it))
        }
    }

    @GetMapping("/messages/count")
    suspend fun showCountOfMessages(): CountMessages = flow {
        val response = CompletableDeferred<Int>()
        readMessageService.getBy(GetCountOfMessages(response))
        val count = response.await()
        emit(CountMessages(count))
    }.single()

    data class CountMessages(val count: Int)
}