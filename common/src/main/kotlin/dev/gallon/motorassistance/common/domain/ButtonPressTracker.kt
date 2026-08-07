package dev.gallon.motorassistance.common.domain

/**
 * Converts a held button state into one event per physical press.
 */
class ButtonPressTracker {
    private var wasPressed = false

    fun update(pressed: Boolean): Boolean {
        val justPressed = pressed && !wasPressed
        wasPressed = pressed
        return justPressed
    }
}
