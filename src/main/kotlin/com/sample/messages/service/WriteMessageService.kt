package com.sample.messages.service

import com.sample.messages.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.newSingleThreadContext
import org.springframework.stereotype.Service

interface WriteMessageService {
    suspend fun saveBy(writeCounterMessage: WriteCounterMessage)
}

@Service
class WriteMessageActorService(private val containerService: MessageContainerService)
    : WriteMessageService {

    private val messageActorRead: SendChannel<WriteCounterMessage> = CoroutineScope(newSingleThreadContext("messages")).actor {
        for (msg in channel) {
            when (msg) {
                is AddMessageRead -> containerService.addMessage(msg.message)
            }
        }
    }

    override suspend fun saveBy(writeCounterMessage: WriteCounterMessage) {
        messageActorRead.send(writeCounterMessage)
    }
}

sealed class WriteCounterMessage
class AddMessageRead(val message: Message) : WriteCounterMessage()