package com.sample.messages.service

import com.sample.messages.model.Message
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageContainerService {

    val messages: MutableList<Message> = Vector()

    fun addMessage(message: Message) {
        this.messages.add(message)
    }
}
