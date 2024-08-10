package de.davis.powercontrol.core.prenentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HapticSwipeToDismissBox(
    backgroundContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    state: SwipeToDismissBoxState = rememberSwipeToDismissBoxState(),
    content: @Composable RowScope.() -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(key1 = state.targetValue) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    SwipeToDismissBox(
        modifier = modifier.clip(MaterialTheme.shapes.medium),
        state = state,
        enableDismissFromStartToEnd = false,
        backgroundContent = backgroundContent,
        content = content
    )
}