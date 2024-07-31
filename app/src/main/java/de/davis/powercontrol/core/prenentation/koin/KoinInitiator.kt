package de.davis.powercontrol.core.prenentation.koin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import de.davis.powercontrol.core.di.coreKoinModule
import de.davis.powercontrol.dashboard.di.dashboardKoinModule
import de.davis.powercontrol.device.di.deviceKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.LocalKoinApplication
import org.koin.compose.LocalKoinScope
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.startKoin
import org.koin.core.module.KoinApplicationDslMarker
import org.koin.dsl.KoinAppDeclaration
import org.koin.mp.KoinPlatformTools

@KoinApplicationDslMarker
@Composable
fun InitiateKoin(appDeclaration: KoinAppDeclaration = {}, content: @Composable () -> Unit) {
    val context = LocalContext.current
    KoinRotationConsistentApplication(
        application = {
            androidLogger()
            androidContext(context)

            modules(deviceKoinModule, dashboardKoinModule, coreKoinModule)

            appDeclaration()
        },
        content = content
    )
}

@OptIn(KoinInternalApi::class)
@Composable
private fun KoinRotationConsistentApplication(
    application: KoinAppDeclaration,
    content: @Composable () -> Unit
) {
    val koin = remember(application) {
        KoinPlatformTools.defaultContext().getOrNull() ?: startKoin(application).koin
    }
    CompositionLocalProvider(
        LocalKoinApplication provides koin,
        LocalKoinScope provides koin.scopeRegistry.rootScope
    ) {
        content()
    }
}