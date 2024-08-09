package de.davis.powercontrol.device.domain.usecases

import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.usecases.ValidateIpv4AddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidateMacAddressUseCase
import de.davis.powercontrol.device.domain.model.IpValidationResult
import de.davis.powercontrol.device.domain.model.MacValidationResult
import de.davis.powercontrol.device.domain.model.ValidationResult
import de.davis.powercontrol.device.domain.repository.DeviceRepository

class ValidateDeviceUseCase(
    private val deviceRepository: DeviceRepository,
    private val validateIp4Address: ValidateIpv4AddressUseCase,
    private val validateMacAddress: ValidateMacAddressUseCase,
) {

    suspend operator fun invoke(device: Device): List<ValidationResult> = with(device) {
        buildList {
            if (name.isBlank())
                add(ValidationResult.NameBlank)


            // TODO maybe own use case for conflict checks
            deviceRepository.getDeviceByIp(ip)?.let {
                if (it.id != id)
                    add(IpValidationResult.IpExists)
            }

            if (!validateIp4Address(ip))
                add(IpValidationResult.IpInvalid)

            mac.takeIf(String::isNotBlank)?.let {
                if (!validateMacAddress(it))
                    add(MacValidationResult.MacInvalid)
            }
        }
    }
}