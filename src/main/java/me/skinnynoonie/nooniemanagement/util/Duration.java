package me.skinnynoonie.nooniemanagement.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class Duration {
    public static final Duration PERMANENT = new Duration(-1);

    private final long time;

    public Duration(long time) {
        this.time = time;
    }

    public @NotNull Map<TimeUnit, Long> toTimeUnits() {
        if (this.isInfinite()) {
            throw new UnsupportedOperationException("can get time units of an infinite time span");
        }

        final long MILLIS_IN_SECOND = 1000;
        final long MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;
        final long MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;
        final long MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

        long timeTemp = this.time;

        long days = timeTemp / MILLIS_IN_DAY;
        timeTemp %= MILLIS_IN_DAY;

        long hours = timeTemp / MILLIS_IN_HOUR;
        timeTemp %= MILLIS_IN_HOUR;

        long minutes = timeTemp / MILLIS_IN_MINUTE;
        timeTemp %= MILLIS_IN_MINUTE;

        long seconds = timeTemp / MILLIS_IN_SECOND;
        timeTemp %= MILLIS_IN_SECOND;

        long millis = timeTemp;

        return Map.of(
                TimeUnit.DAYS, days,
                TimeUnit.HOURS, hours,
                TimeUnit.MINUTES, minutes,
                TimeUnit.SECONDS, seconds,
                TimeUnit.MILLISECONDS, millis
        );
    }

    public long getMillis() {
        return this.time;
    }

    public boolean isFinite() {
        return this.time >= 0;
    }

    public boolean isInfinite() {
        return !this.isFinite();
    }

    public boolean isPermanent() {
        return this.isInfinite();
    }
}
