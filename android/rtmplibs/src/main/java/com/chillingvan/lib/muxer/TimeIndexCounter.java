package com.chillingvan.lib.muxer;

/**
 *
 */
public class TimeIndexCounter {
    private long lastTimeUs;
    private int timeIndex;

    public void calcTotalTime(long currentTimeUs) {
        if (lastTimeUs <= 0) {
            this.lastTimeUs = currentTimeUs;
        }
        int delta = (int) (currentTimeUs - lastTimeUs);
        this.lastTimeUs = currentTimeUs;
        timeIndex += Math.abs(delta / 1000);
    }

    public void reset() {
        lastTimeUs = 0;
        timeIndex = 0;
    }

    public int getTimeIndex() {
        return timeIndex;
    }
}
