package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.`typealias`.MacAddress

class ValidatePartialMacAddressUseCase {

    operator fun invoke(macAddress: MacAddress) = macAddress.matches(REGEX)

    companion object {
        private val REGEX = "^([0-9A-Fa-f]{1,2}[:-]){0,5}([0-9A-Fa-f]{1,2})?$".toRegex()
    }
}