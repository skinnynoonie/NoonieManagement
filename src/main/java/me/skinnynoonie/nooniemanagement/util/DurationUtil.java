package me.skinnynoonie.nooniemanagement.util;

import java.time.Duration;

public final class DurationUtil {
    public static String format(Duration duration) {
        StringBuilder formatBuilder = new StringBuilder();
        appendTimeUnit(formatBuilder, duration.toDaysPart(), "day");
        appendTimeUnit(formatBuilder, duration.toHoursPart(), "hour");
        appendTimeUnit(formatBuilder, duration.toMinutesPart(), "minute");
        appendTimeUnit(formatBuilder, duration.toSecondsPart(), "second");
        return formatBuilder.toString().trim();
    }

    private static void appendTimeUnit(StringBuilder stringBuilder, long time, String singularUnitName) {
        if (time > 0) {
            stringBuilder.append(" ").append(time).append(" ").append(singularUnitName);
            if (time != 1) {
                stringBuilder.append("s");
            }
        }
    }

    private DurationUtil() {
    }
}
