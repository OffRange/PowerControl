package de.davis.powercontrol.device.presentation

import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.device.domain.model.ValidationResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DeviceState(
    val device: Device = Device(),
    val shutdownSequenceConflict: Boolean = false,
    val ipAddressConflict: Boolean = false,
    val stored: Boolean = false,

    val errors: ImmutableList<ValidationResult> = persistentListOf()
)

val DeviceState.createNew: Boolean get() = device.id == 0L