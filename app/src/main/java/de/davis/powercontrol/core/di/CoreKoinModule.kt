package de.davis.powercontrol.core.di

import de.davis.powercontrol.core.domain.usecases.GetDeviceStatusUseCase
import de.davis.powercontrol.core.domain.usecases.ShutdownUseCase
import de.davis.powercontrol.core.domain.usecases.ValidateIpv4AddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidateMacAddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidatePartialIpv4AddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidatePartialMacAddressUseCase
import de.davis.powercontrol.core.domain.usecases.WakeOnLanUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val coreUseCaseKoinModule = module {
    singleOf(::ValidateIpv4AddressUseCase)
    singleOf(::ValidatePartialMacAddressUseCase)
    singleOf(::ValidatePartialIpv4AddressUseCase)
    singleOf(::ValidateMacAddressUseCase)

    singleOf(::ShutdownUseCase)
    singleOf(::WakeOnLanUseCase)

    singleOf(::GetDeviceStatusUseCase)
}

val coreKoinModule = module {
    includes(coreUseCaseKoinModule)
}
