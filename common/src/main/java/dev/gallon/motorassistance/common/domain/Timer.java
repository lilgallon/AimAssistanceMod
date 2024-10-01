package dev.gallon.motorassistance.common.domain;

public class Timer {
    private long startMs;
    private boolean stopped;

    public Timer() {
        startMs = getSystemMilliseconds();
        stopped = true;
    }

    public void start() {
        startMs = getSystemMilliseconds();
        stopped = false;
    }

    public void stop() {
        stopped = true;
    }

    public boolean timeElapsed(long milliseconds) {
        return !stopped && timeElapsed() >= milliseconds;
    }

    long timeElapsed() {
        return getSystemMilliseconds() - startMs;
    }

    public boolean stopped() {
        return stopped;
    }

    private long getSystemMilliseconds() {
        return System.nanoTime() / 1000000L;
    }
}
