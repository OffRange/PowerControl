package de.davis.powercontrol.core.prenentation

sealed interface ItemClickEvent {

    data object ItemShort : ItemClickEvent
    data object ItemLong : ItemClickEvent
    data object Icon : ItemClickEvent
}