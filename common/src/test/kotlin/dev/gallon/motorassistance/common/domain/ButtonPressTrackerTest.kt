package dev.gallon.motorassistance.common.domain

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ButtonPressTrackerTest {
    @Test
    fun `emits once for each press`() {
        val tracker = ButtonPressTracker()

        assertFalse(tracker.update(false))
        assertTrue(tracker.update(true))
        assertFalse(tracker.update(true))
        assertFalse(tracker.update(false))
        assertTrue(tracker.update(true))
    }
}
