package de.davis.powercontrol.dashboard.presentation

import de.davis.powercontrol.core.prenentation.ItemEvent

data class DeviceInteraction(val ip: String, val event: ItemEvent)