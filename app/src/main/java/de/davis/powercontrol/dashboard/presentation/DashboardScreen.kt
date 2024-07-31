package de.davis.powercontrol.dashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.davis.powercontrol.R
import de.davis.powercontrol.core.prenentation.Destination
import de.davis.powercontrol.core.prenentation.ItemClickEvent
import de.davis.powercontrol.core.prenentation.koin.KoinInitiatedAppTheme
import de.davis.powercontrol.device.presentation.Register
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(onNavigateEvent: (Destination) -> Unit) {

    val viewModel = koinViewModel<DashboardViewModel>()
    val devices by viewModel.devices.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onNavigateEvent(Register())
                },
                text = { Text(text = stringResource(id = R.string.add_device)) },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            DashboardContent(
                devices = devices.toImmutableList(),
                onInteract = {
                    when (it.event) {
                        ItemClickEvent.ItemShort -> onNavigateEvent(Register(it.ip))
                        ItemClickEvent.ItemLong -> {}
                        ItemClickEvent.Icon -> viewModel.bootOrShutdown(it.ip)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun DashboardScreenPreview() {
    KoinInitiatedAppTheme {
        DashboardScreen {}
    }
}