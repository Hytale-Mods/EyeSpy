package com.jarhax.eyespy.impl.util;

import java.text.DecimalFormat;

/**
 * A very simple timer used for profiling code. This timer should only be used to give a rough idea of how long things
 * take. It is not very precise and should not be relied on for time sensitive tasks.
 */
public class Timer {

    /**
     * Formats time to 3 decimal places, used for formatted MS timing.
     */
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("#.###");

    private final long startTime;
    private long endTime = -1;

    /**
     * Creates and starts a new timer.
     */
    public Timer() {
        this.startTime = System.nanoTime();
    }

    /**
     * Stops the timer if it has not already been stopped.
     */
    public void stop() {
        if (endTime < 0) {
            endTime = System.nanoTime();
        }
    }

    /**
     * Stops the timer if it has not already been stopped, and provides the elapsed time as ms.
     *
     * @return The elapsed ms time.
     */
    public float msTime() {
        this.stop();
        return (endTime - startTime) / 1_000_0000f;
    }

    /**
     * Stops the timer if it has not already been stopped, and provides the elapsed time in ms as a formatted string.
     *
     * @return The elapsed time as a formatted string.
     */
    public String msTimeFormatted() {
        return TIME_FORMAT.format(this.msTime());
    }
}