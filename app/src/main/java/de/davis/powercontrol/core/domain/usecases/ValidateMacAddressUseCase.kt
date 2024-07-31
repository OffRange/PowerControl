package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.`typealias`.MacAddress

class ValidateMacAddressUseCase {

    operator fun invoke(macAddress: MacAddress) = macAddress.matches(REGEX)

    companion object {
        private val REGEX = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$".toRegex()
    }
}