package de.davis.powercontrol.dashboard.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.davis.powercontrol.R
import de.davis.powercontrol.core.domain.models.Operation
import de.davis.powercontrol.core.domain.models.Schedule
import de.davis.powercontrol.core.prenentation.Destination
import de.davis.powercontrol.core.prenentation.ItemEvent
import de.davis.powercontrol.core.prenentation.koin.KoinInitiatedAppTheme
import de.davis.powercontrol.dashboard.presentation.component.TimePickerDialog
import de.davis.powercontrol.device.presentation.Register
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(onNavigateEvent: (Destination) -> Unit) {

    val viewModel = koinViewModel<DashboardViewModel>()
    val state by viewModel.state.collectAsState()

    val closeDialog = {
        viewModel.updateDialog(DialogType.Closed)
    }

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
                devices = state.devices,
                onInteract = {
                    when (it.event) {
                        ItemEvent.ItemShortClicked -> onNavigateEvent(Register(it.ip))
                        ItemEvent.ItemLongClicked -> {}
                        ItemEvent.IconClicked -> viewModel.updateDialog(DialogType.When(it.ip))
                        ItemEvent.Delete -> viewModel.delete(it.ip)
                    }
                }
            )

            AnimatedContent(targetState = state.dialogType, label = "Animate-Dialog") {
                when (it) {
                    is DialogType.When -> {
                        AlertDialog(
                            modifier = Modifier.fillMaxWidth(),
                            onDismissRequest = closeDialog,
                            confirmButton = {
                                Button(onClick = {
                                    viewModel.scheduleOperation(
                                        Operation.ShutdownOrBoot,
                                        it.ip,
                                        Schedule.Now
                                    )
                                    closeDialog()
                                }) {
                                    Text(text = stringResource(id = R.string.now))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    viewModel.updateDialog(DialogType.TimePicker(it.ip))
                                }) {
                                    Text(text = stringResource(id = R.string.schedule))
                                }
                            },
                            title = { Text(text = stringResource(id = R.string.`when`)) },
                            text = {
                                //TODO create a generic string res that takes the specific operation into account
                                Text(text = "Please select when you'd like to perform this operation. If you choose 'Schedule', you'll be able to set a specific time for your device to carry out the process.")
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.PowerSettingsNew,
                                    contentDescription = null
                                )
                            }
                        )
                    }

                    is DialogType.TimePicker -> {
                        TimePickerDialog(
                            modifier = Modifier.fillMaxWidth(),
                            onClose = closeDialog,
                            scheduled = { localDateTime ->
                                viewModel.scheduleOperation(
                                    Operation.ShutdownOrBoot,
                                    it.ip,
                                    Schedule.Scheduled(localDateTime)
                                )
                            }
                        )
                    }

                    DialogType.Closed -> {}
                }
            }
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