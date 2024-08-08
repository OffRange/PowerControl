package de.davis.powercontrol.dashboard.presentation

import androidx.compose.runtime.Immutable
import de.davis.powercontrol.core.domain.models.Device
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DashboardState(
    val devices: ImmutableList<Device> = persistentListOf(),
    val dialogType: DialogType = DialogType.Closed
)