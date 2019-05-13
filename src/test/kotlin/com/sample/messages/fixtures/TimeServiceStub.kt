package com.sample.messages.fixtures

import com.sample.messages.service.TimeService
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.*

@Profile("test")
@Primary
@Component
class TimeServiceStub : TimeService() {

    private val zoneOffset = ZoneOffset.UTC

    var now: ZonedDateTime = ZonedDateTime.of(LocalDate.of(2019, 3, 1), LocalTime.of(0, 0), zoneOffset)

    override fun nowDateTime(): LocalDateTime {
        return now.toLocalDateTime()
    }

    fun timeTravelTo(newTimestamp: LocalDateTime) {
        now = ZonedDateTime.of(newTimestamp, zoneOffset)
    }
}