package de.davis.powercontrol.core.prenentation

sealed interface ItemEvent {

    data object ItemShortClicked : ItemEvent
    data object ItemLongClicked : ItemEvent
    data object IconClicked : ItemEvent
    data object Delete : ItemEvent
}