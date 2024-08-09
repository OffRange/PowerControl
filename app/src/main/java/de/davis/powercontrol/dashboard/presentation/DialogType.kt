package de.davis.powercontrol.dashboard.presentation

import de.davis.powercontrol.core.domain.models.PowerOperation
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface DialogType {
    data class ScheduleDialog(
        val ip: IpAddress,
        val availablePowerOperations: ImmutableList<PowerOperation> = persistentListOf()
    ) : DialogType

    data class TimePicker(val ip: IpAddress, val operation: PowerOperation) : DialogType

    data object Closed : DialogType
}