package de.davis.powercontrol.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import de.davis.powercontrol.core.domain.models.DeviceStatus
import de.davis.powercontrol.core.domain.models.PowerOperation
import de.davis.powercontrol.core.domain.models.Schedule
import de.davis.powercontrol.core.domain.models.ScheduledOperation
import de.davis.powercontrol.core.domain.models.getRemainingTime
import de.davis.powercontrol.core.domain.`typealias`.IpAddress
import de.davis.powercontrol.core.domain.usecases.GetDeviceStatusUseCase
import de.davis.powercontrol.core.domain.usecases.GetScheduledOperationUseCase
import de.davis.powercontrol.core.worker.PowerControlWorker
import de.davis.powercontrol.device.domain.repository.DeviceRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val workManager: WorkManager,
    private val getDeviceStatus: GetDeviceStatusUseCase,
    private val getScheduledOperation: GetScheduledOperationUseCase,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val _noStatusDevices =
        deviceRepository.observeAllDevices().distinctUntilChanged().stateIn(
            viewModelScope, SharingStarted.Eagerly, emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _devices = _noStatusDevices.flatMapLatest { deviceList ->
        val statusFlows = deviceList.map { device ->
            flow {
                emit(device.copy(status = DeviceStatus.Pending))
                while (viewModelScope.isActive) {
                    val status = getDeviceStatus(device)
                    val scheduledOperation = getScheduledOperation(device)
                    emit(device.copy(status = status, scheduledOperation = scheduledOperation))
                    delay(5000) //TODO improve scheduled operation updates
                }
            }.distinctUntilChanged()
        }
        combine(statusFlows) { it.toList() }
    }

    private val _dialogType = MutableStateFlow<DialogType>(DialogType.Closed)

    val state = combine(_devices, _dialogType) { devices, dialogType ->
        DashboardState(devices.toImmutableList(), dialogType)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DashboardState())

    fun iconClicked(ip: IpAddress) {
        if (state.value.devices.find { device -> device.ip == ip }?.scheduledOperation is ScheduledOperation.Scheduled) {
            PowerControlWorker.cancel(ip, workManager)
        } else {
            updateDialog(DialogType.ScheduleDialog(ip))
        }
    }

    fun updateDialog(dialogType: DialogType) {
        viewModelScope.launch {
            _dialogType.update {
                when (dialogType) {
                    is DialogType.ScheduleDialog -> {
                        dialogType.copy(
                            availablePowerOperations = if (state.value.devices.find { device -> device.ip == dialogType.ip }?.status == DeviceStatus.Online) {
                                PowerOperation.entries.filter { entry -> entry != PowerOperation.Boot }
                                    .toImmutableList()
                            } else {
                                persistentListOf(PowerOperation.Boot)
                            }
                        )
                    }

                    else -> dialogType
                }
            }
        }
    }

    fun scheduleOperation(operation: PowerOperation, ip: IpAddress, schedule: Schedule) {
        viewModelScope.launch {
            PowerControlWorker.schedule(
                operation = operation,
                ip = ip,
                initialDelay = schedule.getRemainingTime(),
                workManager = workManager
            )
        }
    }

    fun delete(ip: IpAddress) {
        viewModelScope.launch {
            deviceRepository.deleteDevice(ip)
        }
    }
}