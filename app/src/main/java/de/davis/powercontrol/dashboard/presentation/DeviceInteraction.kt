package de.davis.powercontrol.dashboard.presentation

import de.davis.powercontrol.core.prenentation.ItemClickEvent

data class DeviceInteraction(val ip: String, val event: ItemClickEvent)