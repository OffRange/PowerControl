package de.davis.powercontrol.dashboard.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import de.davis.powercontrol.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    scheduled: (LocalDateTime) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )
    Dialog(
        onDismissRequest = { onClose() },
        properties = DialogProperties()
    ) {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp /*TitlePadding*/)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.select_time),
                        style = MaterialTheme.typography.headlineSmall,
                        color = AlertDialogDefaults.titleContentColor,
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(bottom = 24.dp /*TextPadding*/)
                        .align(Alignment.CenterHorizontally)
                ) {
                    TimePicker(state = state)
                }

                Button(
                    onClick = {
                        scheduled(LocalTime.of(state.hour, state.minute).atDate(LocalDate.now()))
                        onClose()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(id = R.string.schedule))
                }
            }
        }
    }
}