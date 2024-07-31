package de.davis.powercontrol.core.domain.usecases

import de.davis.powercontrol.core.domain.`typealias`.IpAddress

class FormatIpAddressUseCase {

    operator fun invoke(ipAddress: String): IpAddress =
        ipAddress.replace("[^0-9.]".toRegex(), "")
            .split('.')
            .flatMap { s -> s.chunked(3) }
            .joinToString(
                ".",
                postfix = if (ipAddress.length % 3 == 0 && ipAddress.length in (1..11)) "." else ""
            )
}