package de.davis.powercontrol.device.di

import androidx.room.Room
import de.davis.powercontrol.device.data.local.database.DEVICES_DB_NAME
import de.davis.powercontrol.device.data.local.database.DeviceDatabase
import de.davis.powercontrol.device.data.repository.DeviceRepositoryImpl
import de.davis.powercontrol.device.domain.repository.DeviceRepository
import de.davis.powercontrol.device.domain.usecases.ValidateDeviceUseCase
import de.davis.powercontrol.device.presentation.DeviceViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

internal val deviceDatabaseKoinModule = module {
    singleOf(::DeviceRepositoryImpl) bind DeviceRepository::class

    single {
        Room.databaseBuilder(androidContext(), DeviceDatabase::class.java, DEVICES_DB_NAME).build()
    }

    fun Scope.getDatabase() = get<DeviceDatabase>()
    single {
        getDatabase().deviceDao()
    }
}

internal val deviceUseCaseKoinModule = module {
    singleOf(::ValidateDeviceUseCase)
}

internal val deviceViewModelKoinModule = module {
    viewModelOf(::DeviceViewModel)
}

val deviceKoinModule = module {
    includes(deviceUseCaseKoinModule)
    includes(deviceViewModelKoinModule)
    includes(deviceDatabaseKoinModule)
}