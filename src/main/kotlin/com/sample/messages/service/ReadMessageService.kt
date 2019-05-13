package com.sample.messages.service

import com.sample.messages.model.Message
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import org.springframework.stereotype.Service

interface ReadMessageService {
    suspend fun getBy(readCounterMessage: ReadCounterMessage)
}

@Service
@ObsoleteCoroutinesApi
class ReadMessageActorService(private val containerService: MessageContainerService) : ReadMessageService {
    private val messageActorRead: SendChannel<ReadCounterMessage> = GlobalScope.actor {
        for (msg in channel) {
            when (msg) {
                is GetMessages -> msg.response.complete(containerService.messages)
                is GetCountOfMessages -> {
                    val count = containerService.messages.count()
                    msg.response.complete(count)
                }
            }
        }
    }

    override suspend fun getBy(readCounterMessage: ReadCounterMessage) {
        messageActorRead.send(readCounterMessage)
    }
}

sealed class ReadCounterMessage
class GetMessages(val response: CompletableDeferred<List<Message>>) : ReadCounterMessage()
class GetCountOfMessages(val response: CompletableDeferred<Int>) : ReadCounterMessage()