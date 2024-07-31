package de.davis.powercontrol.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.core.domain.usecases.ShutdownUseCase
import de.davis.powercontrol.device.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val shutdown: ShutdownUseCase,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    val devices = deviceRepository.observeAllDevices().distinctUntilChanged().stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    fun bootOrShutdown(ip: IpAddress) {
        viewModelScope.launch {
            deviceRepository.getDeviceByIp(ip)?.let { shutdown(it) }
        }
    }
}