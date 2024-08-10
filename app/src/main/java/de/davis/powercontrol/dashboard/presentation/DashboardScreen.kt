package de.davis.powercontrol.dashboard.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import de.davis.powercontrol.core.domain.models.Schedule
import de.davis.powercontrol.core.prenentation.Destination
import de.davis.powercontrol.core.prenentation.ItemEvent
import de.davis.powercontrol.core.prenentation.koin.KoinInitiatedAppTheme
import de.davis.powercontrol.dashboard.presentation.component.ScheduleDialog
import de.davis.powercontrol.dashboard.presentation.component.ScheduleDialogUpdate
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
                        ItemEvent.IconClicked -> viewModel.iconClicked(it.ip)

                        ItemEvent.Delete -> viewModel.delete(it.ip)
                    }
                }
            )

            AnimatedContent(targetState = state.dialogType, label = "Animate-Dialog") {
                when (it) {
                    is DialogType.ScheduleDialog -> {
                        ScheduleDialog(
                            modifier = Modifier.fillMaxWidth(),
                            closeDialog = closeDialog,
                            onUpdate = { updateType ->
                                when (updateType) {
                                    is ScheduleDialogUpdate.RunOperationNow -> viewModel.scheduleOperation(
                                        updateType.operation,
                                        it.ip,
                                        Schedule.Now
                                    )

                                    is ScheduleDialogUpdate.SelectTime -> viewModel.updateDialog(
                                        DialogType.TimePicker(
                                            it.ip,
                                            updateType.operation
                                        )
                                    )
                                }
                            },
                            availablePowerOperations = it.availablePowerOperations,
                        )
                    }

                    is DialogType.TimePicker -> {
                        TimePickerDialog(
                            modifier = Modifier.fillMaxWidth(),
                            onClose = closeDialog,
                            scheduled = { localDateTime ->
                                viewModel.scheduleOperation(
                                    it.operation,
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