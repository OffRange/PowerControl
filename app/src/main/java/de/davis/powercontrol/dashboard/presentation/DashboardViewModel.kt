package de.davis.powercontrol.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.davis.powercontrol.core.domain.models.DeviceStatus
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.core.domain.usecases.GetDeviceStatusUseCase
import de.davis.powercontrol.core.domain.usecases.ShutdownUseCase
import de.davis.powercontrol.core.domain.usecases.WakeOnLanUseCase
import de.davis.powercontrol.device.domain.repository.DeviceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val shutdown: ShutdownUseCase,
    private val boot: WakeOnLanUseCase,
    private val getDeviceStatus: GetDeviceStatusUseCase,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val _noStatusDevices =
        deviceRepository.observeAllDevices().distinctUntilChanged().stateIn(
            viewModelScope, SharingStarted.Eagerly, emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val devices = _noStatusDevices.flatMapLatest { deviceList ->
        val statusFlows = deviceList.map { device ->
            flow {
                emit(device.copy(status = DeviceStatus.Pending))
                while (viewModelScope.isActive) {
                    val status = getDeviceStatus(device)
                    emit(device.copy(status = status))

                    delay(5000)
                }
            }.distinctUntilChanged()
        }
        combine(statusFlows) { it.toList() }
    }.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    fun bootOrShutdown(ip: IpAddress) {
        viewModelScope.launch {
            deviceRepository.getDeviceByIp(ip)?.let {
                val status = getDeviceStatus(it)
                when (status) {
                    DeviceStatus.Online -> shutdown(it)
                    DeviceStatus.Offline -> boot(it)
                    else -> {}
                }
            }
        }
    }

    fun delete(ip: IpAddress) {
        viewModelScope.launch {
            deviceRepository.deleteDevice(ip)
        }
    }
}