package com.sample.messages.fixtures

import com.sample.messages.service.MessageContainerService
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Primary
@Service
class MessageContainerServiceStub : MessageContainerService() {

    fun clear() {
        messages.clear()
    }
}
