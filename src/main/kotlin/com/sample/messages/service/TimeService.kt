package com.sample.messages.service

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class TimeService {

    val ZONE_ID: ZoneId = ZoneId.of("Europe/Amsterdam")

    fun nowDateTime(): LocalDateTime {
        return LocalDateTime.now(ZONE_ID)
    }
}