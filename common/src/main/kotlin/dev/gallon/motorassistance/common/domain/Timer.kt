package dev.gallon.motorassistance.common.domain

class Timer {

    private var startMs: Long
    private var stopped: Boolean

    init {
        startMs = getSystemMilliseconds()
        stopped = true
    }

    fun start() {
        startMs = getSystemMilliseconds()
        stopped = false
    }

    fun stop() {
        stopped = true
    }

    fun timeElapsed(milliseconds: Long): Boolean = !stopped && timeElapsed() >= milliseconds
    fun timeElapsed(): Long = getSystemMilliseconds() - startMs
    fun stopped(): Boolean = stopped

    private fun getSystemMilliseconds() = System.nanoTime() / 1000000L
}
