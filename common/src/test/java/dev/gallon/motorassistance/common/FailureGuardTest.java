package dev.gallon.motorassistance.common;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FailureGuardTest {
    @Test
    void disablesAfterRuntimeExceptionAndReportsOnlyOnce() {
        AtomicInteger reports = new AtomicInteger();
        AtomicInteger executions = new AtomicInteger();
        FailureGuard guard = new FailureGuard((phase, failure) -> reports.incrementAndGet());

        assertFalse(guard.run("tick", () -> {
            executions.incrementAndGet();
            throw new IllegalStateException("boom");
        }));
        assertFalse(guard.run("render", executions::incrementAndGet));

        assertTrue(guard.isDisabled());
        assertEquals(1, reports.get());
        assertEquals(1, executions.get());
    }

    @Test
    void disablesAfterLinkageError() {
        AtomicInteger reports = new AtomicInteger();
        FailureGuard guard = new FailureGuard((phase, failure) -> reports.incrementAndGet());

        assertFalse(guard.run("HUD rendering", () -> {
            throw new NoClassDefFoundError("missing optional runtime class");
        }));

        assertTrue(guard.isDisabled());
        assertEquals(1, reports.get());
    }

    @Test
    void doesNotCatchOtherJvmErrors() {
        FailureGuard guard = new FailureGuard((phase, failure) -> {
        });

        assertThrows(AssertionError.class, () -> guard.run("test", () -> {
            throw new AssertionError("not recoverable by the mod");
        }));
        assertFalse(guard.isDisabled());
        assertTrue(guard.run("next frame", () -> {
        }));
    }
}
