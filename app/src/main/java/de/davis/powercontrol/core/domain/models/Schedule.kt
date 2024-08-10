package de.davis.powercontrol.core.domain.models

import java.time.Duration
import java.time.LocalDateTime

sealed interface Schedule {
    data object Now : Schedule
    data class Scheduled(val localDateTime: LocalDateTime) : Schedule
}

fun Schedule.getRemainingTime() = when (this) {
    is Schedule.Now -> 0
    is Schedule.Scheduled -> {
        val scheduleTime = localDateTime.withNano(0).withSecond(0).let {
            if (it.isBefore(LocalDateTime.now())) it.plusDays(1) else it
        }

        Duration.between(LocalDateTime.now(), scheduleTime).toMillis().coerceAtLeast(0)
    }
}