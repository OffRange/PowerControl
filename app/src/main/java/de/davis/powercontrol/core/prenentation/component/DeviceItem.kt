package de.davis.powercontrol.core.prenentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.davis.powercontrol.R
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.models.DeviceStatus
import de.davis.powercontrol.core.domain.models.couldAcceptOperations
import de.davis.powercontrol.core.prenentation.ItemClickEvent
import de.davis.powercontrol.core.prenentation.theme.AppTheme
import de.davis.powercontrol.core.prenentation.theme.contentColorFor
import de.davis.powercontrol.core.prenentation.theme.extendedColorScheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    device: Device,
    onClicked: (ItemClickEvent) -> Unit
) = with(device) {
    ListItem(
        modifier = modifier.combinedClickable(
            onLongClick = { onClicked(ItemClickEvent.ItemLong) },
            onClick = { onClicked(ItemClickEvent.ItemShort) }
        ),
        headlineContent = { Text(text = name) },
        supportingContent = {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val containerColor = when (status) {
                    DeviceStatus.Online -> MaterialTheme.extendedColorScheme.success.colorContainer
                    else -> MaterialTheme.colorScheme.errorContainer
                }

                Badge(
                    modifier = Modifier.alignByBaseline(),
                    containerColor = containerColor,
                    contentColor = MaterialTheme.extendedColorScheme.success.contentColorFor(
                        containerColor
                    ).takeOrElse {
                        LocalContentColor.current
                    },
                ) {
                    val statusText = when (status) {
                        DeviceStatus.Online -> R.string.online
                        DeviceStatus.Offline -> R.string.offline
                        DeviceStatus.Pending -> R.string.pending
                    }
                    Text(text = stringResource(id = statusText))
                }
                Text(text = ip, modifier = Modifier.alignByBaseline())
            }
        },
        trailingContent = {
            IconButton(
                onClick = { onClicked(ItemClickEvent.Icon) },
                enabled = couldAcceptOperations
            ) {
                Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = null)
            }
        }
    )
}

@Composable
@Preview
fun ItemPreview() {
    AppTheme(darkTheme = true) {
        DeviceItem(
            device = Device(1, "Test", "192.168.1.1", status = DeviceStatus.Online),
            onClicked = {}
        )
    }
}