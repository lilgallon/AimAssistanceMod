package dev.gallon.motorassistance.fabric.events

sealed interface Event

data object RenderEvent : Event

data object TickEvent : Event

data class MouseMoveEvent(
    val x: Double,
    val y: Double
) : Event

data object LeftMouseClickEvent : Event
