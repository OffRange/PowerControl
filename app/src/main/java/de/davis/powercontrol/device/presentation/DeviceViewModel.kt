package de.davis.powercontrol.device.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.usecases.ValidatePartialIpv4AddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidatePartialMacAddressUseCase
import de.davis.powercontrol.device.domain.model.IpValidationResult
import de.davis.powercontrol.device.domain.model.ValidationResult
import de.davis.powercontrol.device.domain.repository.DeviceRepository
import de.davis.powercontrol.device.domain.usecases.ValidateDeviceUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class DeviceViewModel(
    private val deviceRepository: DeviceRepository,
    private val validateDevice: ValidateDeviceUseCase,
    private val validatePartialIpv4Address: ValidatePartialIpv4AddressUseCase,
    private val validatePartialMacAddress: ValidatePartialMacAddressUseCase,
) : ViewModel() {

    private val _ipAddressFlow = MutableStateFlow("")
    private val _ipConflictFlow = MutableStateFlow(false)
    private val _storedFlow = MutableStateFlow(false)
    private val _errorsFlow = MutableStateFlow(listOf<ValidationResult>())

    init {
        viewModelScope.launch {
            _ipAddressFlow.debounce(300)
                .distinctUntilChanged()
                .collectLatest { ip ->
                    _ipConflictFlow.update {
                        deviceRepository.getDeviceByIp(ip)?.id?.let { fetchedId ->
                            fetchedId != state.value.device.id
                        } ?: false
                    }
                }
        }
    }

    private val _deviceFlow = MutableStateFlow(Device())
    val state = combine(
        _deviceFlow,
        _ipAddressFlow,
        _ipConflictFlow,
        _errorsFlow,
        _storedFlow
    ) { device, ipAddress, ipConflict, errors, stored ->
        DeviceState(
            device = device.copy(ip = ipAddress),
            ipAddressConflict = ipConflict,
            errors = errors.toMutableList()
                .apply { if (ipConflict) add(IpValidationResult.IpExists) }
                .toImmutableList(),
            stored = stored
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, DeviceState())

    fun loadDevice(ip: String) {
        viewModelScope.launch {
            _deviceFlow.update {
                (deviceRepository.getDeviceByIp(ip) ?: Device()).also { device ->
                    _ipAddressFlow.update { device.ip }
                }
            }
        }
    }

    fun onEvent(event: DeviceUiEvent) = with(event) {
        when (this) {
            is DeviceUiEvent.NameChanged -> updateDevice { it.copy(name = name) }
            is DeviceUiEvent.IpAddressChanged -> {
                if (validatePartialIpv4Address(ipAddress))
                    _ipAddressFlow.update { ipAddress }

                Unit
            }

            is DeviceUiEvent.MacAddressChanged -> {
                if (validatePartialMacAddress(macAddress))
                    updateDevice { it.copy(mac = macAddress) }

                Unit
            }

            is DeviceUiEvent.PasswordChanged -> updateDevice { it.copy(password = rawPassword) }
            is DeviceUiEvent.ShutdownSequenceChanged -> updateDevice { it.copy(shutdownSequence = shutdownSequence) }
            DeviceUiEvent.SubmitDevice -> {
                viewModelScope.launch {
                    val device = state.value.device.let {
                        it.copy(ip = it.ip, mac = it.mac)
                    }
                    val errors = validateDevice(device)

                    if (errors.isEmpty()) {
                        deviceRepository.upsertDevice(device)
                        _storedFlow.update { true }
                        return@launch
                    }

                    _errorsFlow.update { errors }
                }
            }
        }
    }

    private fun updateDevice(function: (Device) -> Device) {
        _deviceFlow.update(function)
    }
}