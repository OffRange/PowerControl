package de.davis.powercontrol.dashboard.presentation.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.davis.powercontrol.R
import de.davis.powercontrol.core.domain.models.PowerOperation
import de.davis.powercontrol.core.prenentation.koin.KoinInitiatedAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ScheduleDialog(
    modifier: Modifier = Modifier,
    availablePowerOperations: ImmutableList<PowerOperation>,
    closeDialog: () -> Unit,
    onUpdate: (ScheduleDialogUpdate) -> Unit,
) {
    var selected by remember {
        mutableStateOf(availablePowerOperations.first())
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = closeDialog,
        confirmButton = {
            Button(onClick = {
                onUpdate(ScheduleDialogUpdate.RunOperationNow(selected))
                closeDialog()
            }) {
                Text(text = stringResource(id = R.string.now))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onUpdate(ScheduleDialogUpdate.SelectTime(selected))
            }) {
                Text(text = stringResource(id = R.string.schedule))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.dialog_select_operation_title))
        },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = stringResource(id = R.string.dialog_select_operation_message))

                HorizontalDivider()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .selectableGroup()
                ) {
                    availablePowerOperations.forEach { powerOperation ->
                        PowerControlRadioButton(
                            selected = selected == powerOperation,
                            item = powerOperation,
                            onClick = { selected = it }
                        ) { operation ->
                            stringResource(
                                id = when (operation) {
                                    PowerOperation.Shutdown -> {
                                        R.string.shutdown
                                    }

                                    PowerOperation.Boot -> R.string.boot
                                    PowerOperation.Restart -> R.string.reboot
                                    PowerOperation.Logout -> R.string.logout
                                }
                            )
                        }
                    }
                }
                HorizontalDivider()
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null
            )
        }
    )
}

@Preview
@Composable
private fun ScheduleDialogPreview() {
    KoinInitiatedAppTheme {
        ScheduleDialog(
            closeDialog = {},
            onUpdate = {},
            availablePowerOperations = PowerOperation.entries.toImmutableList()
        )
    }
}

sealed class ScheduleDialogUpdate(val operation: PowerOperation) {
    class RunOperationNow(operation: PowerOperation) : ScheduleDialogUpdate(operation)
    class SelectTime(operation: PowerOperation) : ScheduleDialogUpdate(operation)
}

@Composable
fun <I> PowerControlRadioButton(
    selected: Boolean,
    item: I,
    onClick: (I) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textFromItem: @Composable (I) -> String
) {
    Row(
        modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = { onClick(item) },
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = rememberRipple()
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.minimumInteractiveComponentSize(),
        ) {
            RadioButton(selected = selected, onClick = null)
        }
        Text(text = textFromItem(item))
    }
}