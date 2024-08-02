package de.davis.powercontrol.dashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.davis.powercontrol.R
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.prenentation.component.DeviceItem
import de.davis.powercontrol.core.prenentation.koin.KoinInitiatedAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DashboardContent(devices: ImmutableList<Device>, onInteract: (DeviceInteraction) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            devices.isNotEmpty() -> List(devices = devices, onInteract = onInteract)
            else -> Empty()
        }
    }
}

@Composable
fun Empty() {
    Box(contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = R.string.no_devices_found))
    }
}

@Composable
private fun List(devices: ImmutableList<Device>, onInteract: (DeviceInteraction) -> Unit) {
    LazyColumn {
        items(
            items = devices,
            key = { it.ip }
        ) { device ->
            DeviceItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                device = device,
                onEvent = {
                    onInteract(DeviceInteraction(device.ip, it))
                }
            )
        }
    }
}

@Composable
@Preview
fun DashboardContentPreview() {
    KoinInitiatedAppTheme {
        DashboardContent(persistentListOf()) {}
    }
}