package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.`typealias`.IpAddress

class ValidateIpv4AddressUseCase {

    operator fun invoke(ipAddress: IpAddress) = ipAddress.matches(REGEX)

    companion object {
        private val REGEX =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
                .toRegex()
    }
}