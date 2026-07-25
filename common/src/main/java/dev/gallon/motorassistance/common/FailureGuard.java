package dev.gallon.motorassistance.common;

import java.util.function.BiConsumer;

final class FailureGuard {
    private final BiConsumer<String, Throwable> failureReporter;
    private boolean disabled;

    FailureGuard(BiConsumer<String, Throwable> failureReporter) {
        this.failureReporter = failureReporter;
    }

    boolean run(String phase, Runnable action) {
        if (disabled) {
            return false;
        }

        try {
            action.run();
            return true;
        } catch (RuntimeException | LinkageError failure) {
            disabled = true;
            try {
                failureReporter.accept(phase, failure);
            } catch (RuntimeException | LinkageError ignored) {
                // Reporting must never turn a recoverable mod failure into a Minecraft crash.
            }
            return false;
        }
    }

    boolean isDisabled() {
        return disabled;
    }
}
