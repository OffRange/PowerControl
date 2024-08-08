package de.davis.powercontrol.core.domain.models

import java.time.LocalDateTime
import java.time.ZoneId

sealed interface Schedule {
    data object Now : Schedule
    data class Scheduled(val localDateTime: LocalDateTime) : Schedule
}

fun Schedule.getRemainingTime() = when (this) {
    is Schedule.Now -> 0
    is Schedule.Scheduled -> {
        (localDateTime.atZone(ZoneId.systemDefault()).toInstant()
            .toEpochMilli() - System.currentTimeMillis()).coerceAtLeast(0)
    }
}