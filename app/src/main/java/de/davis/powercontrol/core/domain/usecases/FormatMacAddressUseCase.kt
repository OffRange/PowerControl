package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.`typealias`.MacAddress

class FormatMacAddressUseCase {

    operator fun invoke(macAddress: String): MacAddress =
        macAddress.replace("[^a-f0-6:]".toRegex(), "")
            .split(':')
            .flatMap { s -> s.chunked(2) }
            .joinToString(
                ":",
                postfix = if (macAddress.length % 2 == 0 && macAddress.length in (1..11)) ":" else ""
            )
}