package de.davis.powercontrol.core.domain.models

sealed interface DeviceStatus {
    data object Online : DeviceStatus
    data object Offline : DeviceStatus

    data object Pending : DeviceStatus
}