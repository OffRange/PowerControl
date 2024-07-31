package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.`typealias`.IpAddress

class ValidatePartialIpv4AddressUseCase {

    operator fun invoke(ipAddress: IpAddress) = ipAddress.matches(REGEX)

    companion object {
        private val REGEX =
            "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){0,3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])?$"
                .toRegex()
    }
}