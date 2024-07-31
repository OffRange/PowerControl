package de.davis.powercontrol.core.prenentation.koin

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import de.davis.powercontrol.core.prenentation.theme.AppTheme
import org.koin.dsl.KoinAppDeclaration

@Composable
fun KoinInitiatedAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    appDeclaration: KoinAppDeclaration = {},
    content: @Composable () -> Unit
) {
    AppTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        InitiateKoin(appDeclaration = appDeclaration, content = content)
    }
}