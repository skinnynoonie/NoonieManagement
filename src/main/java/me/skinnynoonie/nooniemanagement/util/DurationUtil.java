package me.skinnynoonie.nooniemanagement.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DurationUtil {
    public static final int MINUTE_TO_SECONDS = 60;
    public static final int HOUR_TO_SECONDS = MINUTE_TO_SECONDS * 60;
    public static final int DAY_TO_SECONDS = 24 * HOUR_TO_SECONDS;
    private static final Pattern NUM_TEXT_PAIR_PATTERN = Pattern.compile("(\\d+[A-z]+)");
    private static final Pattern NUM_TEXT_SPLITTER_PATTERN = Pattern.compile("(?<=\\d)()(?=[A-z])");
    private static final Pattern NUM_AND_ALPHABET_ONLY = Pattern.compile("^[A-Za-z\\d]+$");

    public static Duration parse(@NotNull String input) {
        Preconditions.checkArgument(input != null, "input");
        Preconditions.checkArgument(NUM_AND_ALPHABET_ONLY.matcher(input).matches(), "input %s has illegal characters", input);

        long seconds = 0;

        Matcher matcher = NUM_TEXT_PAIR_PATTERN.matcher(input);
        if (matcher.find()) {
            do {
                String[] numberTextPair = NUM_TEXT_SPLITTER_PATTERN.split(matcher.group());
                long time = Long.parseLong(numberTextPair[0]);
                String timeUnit = numberTextPair[1].toLowerCase();
                seconds += time * switch (timeUnit) {
                    case "d", "day", "days" -> DAY_TO_SECONDS;
                    case "h", "hr", "hour", "hours" -> HOUR_TO_SECONDS;
                    case "m", "min", "minute", "minutes" -> MINUTE_TO_SECONDS;
                    case "s", "sec", "second", "seconds" -> 1;
                    default -> throw new UnsupportedOperationException("time unit %s is not supported".formatted(timeUnit));
                };
            } while (matcher.find());
        } else {
            throw new IllegalArgumentException("input could be parsed");
        }

        return Duration.ofSeconds(seconds);
    }

    public static String format(@NotNull Duration duration) {
        Preconditions.checkArgument(duration != null, "duration");
        if (duration.isZero() || duration.isNegative()) {
            return "0 seconds";
        }

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
