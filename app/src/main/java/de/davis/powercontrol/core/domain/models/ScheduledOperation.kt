package de.davis.powercontrol.core.domain.models

import java.time.LocalDateTime

sealed interface ScheduledOperation {
    data object None : ScheduledOperation
    data class Scheduled(val operation: PowerOperation, val time: LocalDateTime) :
        ScheduledOperation
}