package de.davis.powercontrol.device.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceScreen(ip: String? = null, navigateBack: () -> Unit) {

    val viewModel = koinViewModel<DeviceViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(ip) {
        if (ip != null)
            viewModel.loadDevice(ip)
    }

    LaunchedEffect(key1 = state.stored) {
        if (state.stored)
            navigateBack()

    }

    DeviceContent(deviceState = state, onEvent = viewModel::onEvent)
}