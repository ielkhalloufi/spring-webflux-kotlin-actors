package com.sample.messages.mapper

import com.sample.messages.model.Message
import com.sample.messages.model.MessageView
import java.time.LocalDateTime

object MessageMapper {

    fun toMessageView(message: Message): MessageView {
        return MessageView(message.content)
    }

    fun toMessage(messageView: MessageView, timestamp: LocalDateTime): Message {
        return Message(content = messageView.message, timestamp = timestamp)
    }
}