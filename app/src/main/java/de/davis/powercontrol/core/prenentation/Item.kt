package de.davis.powercontrol.core.prenentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Item(
    modifier: Modifier = Modifier,
    title: String,
    supportingText: String,
    onClicked: (ItemClickEvent) -> Unit
) {
    ListItem(
        modifier = modifier.combinedClickable(
            onLongClick = { onClicked(ItemClickEvent.ItemLong) },
            onClick = { onClicked(ItemClickEvent.ItemShort) }
        ),
        headlineContent = { Text(text = title) },
        supportingContent = { Text(text = supportingText) },
        trailingContent = {
            IconButton(onClick = { onClicked(ItemClickEvent.Icon) }) {
                Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = null)
            }
        }
    )
}

@Composable
@Preview
fun ItemPreview() {
    Item(
        title = "Item",
        supportingText = "IP Address",
        onClicked = {}
    )
}