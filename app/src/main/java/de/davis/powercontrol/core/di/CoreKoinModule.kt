package de.davis.powercontrol.core.di

import androidx.work.WorkManager
import de.davis.powercontrol.core.domain.usecases.GetDeviceStatusUseCase
import de.davis.powercontrol.core.domain.usecases.GetScheduledOperationUseCase
import de.davis.powercontrol.core.domain.usecases.ShutdownUseCase
import de.davis.powercontrol.core.domain.usecases.ValidateIpv4AddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidateMacAddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidatePartialIpv4AddressUseCase
import de.davis.powercontrol.core.domain.usecases.ValidatePartialMacAddressUseCase
import de.davis.powercontrol.core.domain.usecases.WakeOnLanUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val coreUseCaseKoinModule = module {
    singleOf(::ValidateIpv4AddressUseCase)
    singleOf(::ValidatePartialMacAddressUseCase)
    singleOf(::ValidatePartialIpv4AddressUseCase)
    singleOf(::ValidateMacAddressUseCase)

    singleOf(::GetScheduledOperationUseCase)

    singleOf(::ShutdownUseCase)
    singleOf(::WakeOnLanUseCase)

    singleOf(::GetDeviceStatusUseCase)

    single { WorkManager.getInstance(androidApplication()) }
}

val coreKoinModule = module {
    includes(coreUseCaseKoinModule)
}
