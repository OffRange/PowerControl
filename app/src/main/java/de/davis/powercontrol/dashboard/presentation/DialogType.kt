package de.davis.powercontrol.dashboard.presentation

import de.davis.powercontrol.core.domain.`typealias`.IpAddress

sealed interface DialogType {
    data class When(val ip: IpAddress) : DialogType
    data class TimePicker(val ip: IpAddress) : DialogType
    data object Closed : DialogType
}