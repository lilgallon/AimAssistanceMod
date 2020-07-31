package dev.nero.aimassistance.utils;

public class TimeHelper {

    private long lastMS;
    private boolean stopped;

    /**
     * The timer is stopped by default.
     */
    public TimeHelper(){
        this.lastMS = 0L;
        this.stopped = true;
    }

    /**
     * It stops and reset the timer.
     */
    public void stop(){
        this.stopped = true;
        reset();
    }

    /**
     * It starts and reset the timer.
     */
    public void start(){
        this.stopped = false;
        reset();
    }

    /**
     * It resets the timer to 0
     */
    public void reset(){
        this.lastMS = getCurrentMS();
    }

    /**
     * @return true whether the timer is stopped or not
     */
    public boolean isStopped(){
        return stopped;
    }

    /**
     * @param delay the delay in milliseconds.
     * @return true if the delay has been reached and the timer is not stopped.
     */
    public boolean isDelayComplete(long delay) {
        if(this.stopped) return false;
        if(System.nanoTime() / 1000000L - this.lastMS >= delay) {
            return true;
        } else return delay <= 0;
    }

    /**
     * @return the time elapsed since the timer started.
     */
    public long getTimeElapsed() {
        return this.getCurrentMS() - this.lastMS;
    }

    /**
     * @return current system milliseconds
     */
    private long getCurrentMS(){
        return System.nanoTime() / 1000000L;
    }
}