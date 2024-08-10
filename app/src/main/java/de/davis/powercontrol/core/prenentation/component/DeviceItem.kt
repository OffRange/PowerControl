package de.davis.powercontrol.core.prenentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.davis.powercontrol.R
import de.davis.powercontrol.core.domain.models.Device
import de.davis.powercontrol.core.domain.models.DeviceStatus
import de.davis.powercontrol.core.domain.models.PowerOperation
import de.davis.powercontrol.core.domain.models.ScheduledOperation
import de.davis.powercontrol.core.domain.models.couldAcceptOperations
import de.davis.powercontrol.core.prenentation.ItemEvent
import de.davis.powercontrol.core.prenentation.theme.AppTheme
import de.davis.powercontrol.core.prenentation.theme.contentColorFor
import de.davis.powercontrol.core.prenentation.theme.extendedColorScheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    device: Device,
    onEvent: (ItemEvent) -> Unit
) = with(device) {
    val currentOnEvent by rememberUpdatedState(onEvent)

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> true
                else -> return@rememberSwipeToDismissBoxState false
            }
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )

    LaunchedEffect(key1 = state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart)
            currentOnEvent(ItemEvent.Delete)
    }

    HapticSwipeToDismissBox(
        modifier = modifier,
        state = state,
        backgroundContent = {
            SwipeToDismissBoxBackground(state = state, modifier = Modifier.fillMaxSize())
        }
    ) {
        ListItem(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .combinedClickable(
                    onLongClick = { onEvent(ItemEvent.ItemLongClicked) },
                    onClick = { onEvent(ItemEvent.ItemShortClicked) }
                ),
            headlineContent = { Text(text = name) },
            supportingContent = {
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StatusBadge(
                        modifier = Modifier.alignByBaseline(),
                        status = status
                    )
                    Text(text = ip, modifier = Modifier.alignByBaseline())

                    Spacer(modifier = Modifier.weight(1f))

                    if (scheduledOperation is ScheduledOperation.Scheduled) {
                        ScheduleBadge(
                            modifier = Modifier.alignByBaseline(),
                            scheduledOperation = scheduledOperation
                        )
                    }
                }
            },
            trailingContent = {
                IconButton(
                    onClick = { onEvent(ItemEvent.IconClicked) },
                    enabled = couldAcceptOperations
                ) {
                    AnimatedContent(
                        targetState = scheduledOperation is ScheduledOperation.Scheduled,
                        label = "DeviceItem power icon"
                    ) {
                        val icon = when (it) {
                            true -> Icons.Default.AlarmOff
                            else -> Icons.Default.PowerSettingsNew
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissBoxBackground(
    state: SwipeToDismissBoxState,
    modifier: Modifier = Modifier
) {
    val boxBackgroundContainerColor by animateColorAsState(
        when (state.targetValue) {
            SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "DeviceItem boxBackgroundContainerColor"
    )

    val contentColor by animateColorAsState(
        when (state.targetValue) {
            SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.onErrorContainer
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "DeviceItem contentColor"
    )

    Row(
        modifier = modifier
            .background(boxBackgroundContainerColor)
            .padding(horizontal = 16.dp /*ListItemEndPadding*/ + 12.dp /*IconSize / 2*/)
            .clip(MaterialTheme.shapes.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            tint = contentColor,
            contentDescription = null
        )
    }
}

@Composable
private fun ScheduleBadge(
    scheduledOperation: ScheduledOperation.Scheduled,
    modifier: Modifier = Modifier
) {
    Badge(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
    ) {

        val operationName = when (scheduledOperation.operation) {
            PowerOperation.Boot -> R.string.boot
            PowerOperation.Shutdown -> R.string.shutdown
            PowerOperation.Restart -> R.string.reboot
            PowerOperation.Logout -> R.string.logout
        }

        Text(
            text = stringResource(
                id = R.string.scheduled_operation,
                stringResource(id = operationName),
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    .format(scheduledOperation.time)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusBadge(status: DeviceStatus, modifier: Modifier = Modifier) {
    val containerColor = when (status) {
        DeviceStatus.Online -> MaterialTheme.extendedColorScheme.success.colorContainer
        else -> MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = MaterialTheme.extendedColorScheme.success
        .contentColorFor(containerColor)
        .takeOrElse { LocalContentColor.current }

    Badge(
        modifier = modifier.then(
            when (status) {
                DeviceStatus.Online -> {
                    val infiniteTransition =
                        rememberInfiniteTransition(label = "DeviceItem infinite transition for border")
                    val animateFloat by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            tween(1500, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "DeviceItem status badge"
                    )

                    val color1 =
                        MaterialTheme.extendedColorScheme.success.colorContainer
                    val color2 =
                        MaterialTheme.extendedColorScheme.success.onColorContainer

                    Modifier
                        .clip(CircleShape)
                        .padding(1.dp)
                        .drawWithContent {
                            rotate(animateFloat) {
                                drawCircle(
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            color1,
                                            color2,
                                            color1,
                                            color2,
                                            color1
                                        )
                                    ),
                                    radius = size.width,
                                    blendMode = BlendMode.SrcIn,
                                )
                            }
                            drawContent()
                        }
                }

                else -> Modifier
            }
        ),
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        val statusText = when (status) {
            DeviceStatus.Online -> R.string.online
            DeviceStatus.Offline -> R.string.offline
            DeviceStatus.Pending -> R.string.pending
        }
        Text(text = stringResource(id = statusText))
    }
}

@Composable
@Preview
fun ItemPreview() {
    AppTheme(darkTheme = true) {
        DeviceItem(
            device = Device(
                1,
                "Test",
                "192.168.1.1",
                status = DeviceStatus.Online,
                scheduledOperation = ScheduledOperation.Scheduled(
                    operation = PowerOperation.Boot,
                    time = LocalDateTime.now()
                )
            ),
            onEvent = {}
        )
    }
}