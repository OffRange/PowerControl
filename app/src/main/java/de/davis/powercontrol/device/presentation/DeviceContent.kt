package de.davis.powercontrol.device.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.davis.powercontrol.R
import de.davis.powercontrol.core.domain.models.uShutdownSequence
import de.davis.powercontrol.core.prenentation.koin.KoinInitiatedAppTheme
import de.davis.powercontrol.device.domain.model.IpValidationResult
import de.davis.powercontrol.device.domain.model.MacValidationResult
import de.davis.powercontrol.device.domain.model.ValidationResult
import de.davis.powercontrol.device.domain.model.`in`


@OptIn(ExperimentalStdlibApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DeviceContent(deviceState: DeviceState, onEvent: (DeviceUiEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (deviceState.createNew) "Create New" else deviceState.device.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (!deviceState.createNew) {
                            Text(
                                text = deviceState.device.ip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(DeviceUiEvent.SubmitDevice) }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            TextField(
                value = deviceState.device.name,
                onValueChange = {
                    onEvent(DeviceUiEvent.NameChanged(it))
                },
                label = { Text(text = stringResource(id = R.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                isError = ValidationResult.NameBlank in deviceState.errors,
                supportingText = {
                    if (ValidationResult.NameBlank in deviceState.errors) {
                        Text(text = stringResource(id = R.string.name_blank))
                    }
                }
            )

            TextField(
                value = deviceState.device.ip,
                onValueChange = {
                    onEvent(DeviceUiEvent.IpAddressChanged(it))
                },
                label = { Text(text = stringResource(id = R.string.ip_address)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                isError = IpValidationResult `in` deviceState.errors,
                supportingText = {
                    if (IpValidationResult.IpExists in deviceState.errors) {
                        Text(text = stringResource(id = R.string.ip_exists))
                    }

                    if (IpValidationResult.IpInvalid in deviceState.errors) {
                        Text(text = stringResource(id = R.string.ip_invalid))
                    }
                },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                value = deviceState.device.mac ?: "",
                onValueChange = {
                    onEvent(DeviceUiEvent.MacAddressChanged(it))
                },
                label = { Text(text = stringResource(id = R.string.mac_address)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                isError = MacValidationResult `in` deviceState.errors,
                supportingText = {
                    if (MacValidationResult.MacInvalid in deviceState.errors) {
                        Text(text = stringResource(id = R.string.mac_invalid))
                    }
                }
            )

            var passwordHidden by rememberSaveable { mutableStateOf(true) }
            TextField(
                value = deviceState.device.password ?: "",
                onValueChange = {
                    onEvent(DeviceUiEvent.PasswordChanged(it))
                },
                visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                label = { Text(text = stringResource(id = R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

                trailingIcon = {
                    IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        val visibilityIcon =
                            if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        Icon(imageVector = visibilityIcon, contentDescription = null)
                    }
                }
            )

            Column {
                Text(
                    text = "${stringResource(id = R.string.shutdown_sequence)}: 0x${
                        deviceState.device.shutdownSequence.toHexString()
                    } (${deviceState.device.uShutdownSequence})",
                    style = MaterialTheme.typography.bodySmall
                )
                Slider(
                    value = deviceState.device.uShutdownSequence.toFloat(),
                    valueRange = UByte.MIN_VALUE.toFloat()..UByte.MAX_VALUE.toFloat(),
                    steps = UByte.MAX_VALUE.toInt(),
                    onValueChange = {
                        onEvent(DeviceUiEvent.ShutdownSequenceChanged(it.toUInt().toByte()))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterContentPreview() {
    KoinInitiatedAppTheme {
        DeviceContent(DeviceState()) {}
    }
}