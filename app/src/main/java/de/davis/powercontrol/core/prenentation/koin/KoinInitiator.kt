package de.davis.powercontrol.core.prenentation.koin

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import de.davis.powercontrol.core.di.coreKoinModule
import de.davis.powercontrol.dashboard.di.dashboardKoinModule
import de.davis.powercontrol.device.di.deviceKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinApplication
import org.koin.core.module.KoinApplicationDslMarker
import org.koin.dsl.KoinAppDeclaration

@KoinApplicationDslMarker
@Composable
fun InitiateKoin(appDeclaration: KoinAppDeclaration = {}, content: @Composable () -> Unit) {
    val context = LocalContext.current
    KoinApplication(
        application = {
            androidLogger()
            androidContext(context)

            modules(deviceKoinModule, dashboardKoinModule, coreKoinModule)

            appDeclaration()
        },
        content = content
    )
}