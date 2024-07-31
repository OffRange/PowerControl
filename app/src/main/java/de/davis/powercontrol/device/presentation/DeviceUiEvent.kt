package de.davis.powercontrol.device.presentation

import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.core.domain.`typealias`.MacAddress

sealed interface DeviceUiEvent {

    data class NameChanged(val name: String) : DeviceUiEvent
    data class IpAddressChanged(val ipAddress: IpAddress) : DeviceUiEvent
    data class MacAddressChanged(val macAddress: MacAddress) : DeviceUiEvent
    data class PasswordChanged(val rawPassword: String) : DeviceUiEvent
    data class ShutdownSequenceChanged(val shutdownSequence: Byte) : DeviceUiEvent

    data object SubmitDevice : DeviceUiEvent
}