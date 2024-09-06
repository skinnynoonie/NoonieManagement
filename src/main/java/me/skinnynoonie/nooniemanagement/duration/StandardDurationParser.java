package me.skinnynoonie.nooniemanagement.duration;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.durationformat.DurationFormatConfig;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StandardDurationParser implements DurationParser {
    private static final int MINUTE_TO_SECONDS = 60;
    private static final int HOUR_TO_SECONDS = MINUTE_TO_SECONDS * 60;
    private static final int DAY_TO_SECONDS = 24 * HOUR_TO_SECONDS;
    private static final Pattern NUM_TEXT_PAIR_PATTERN = Pattern.compile("(\\d+[A-z]+)");
    private static final Pattern NUM_TEXT_SPLITTER_PATTERN = Pattern.compile("(?<=\\d)()(?=[A-z])");
    private static final Pattern NUM_AND_ALPHABET_ONLY = Pattern.compile("^[A-Za-z\\d]+$");
    private final DurationFormatConfig durationFormatConfig;

    public StandardDurationParser(@NotNull DurationFormatConfig durationFormatConfig) {
        Preconditions.checkArgument(durationFormatConfig != null, "durationFormatConfig");

        this.durationFormatConfig = durationFormatConfig;
    }

    @Override
    public @NotNull Duration parse(@NotNull String input) {
        Preconditions.checkArgument(input != null, "input");
        Preconditions.checkArgument(NUM_AND_ALPHABET_ONLY.matcher(input).matches(), "input %s has illegal characters", input);

        long seconds = 0;

        Matcher matcher = NUM_TEXT_PAIR_PATTERN.matcher(input.toUpperCase());
        if (matcher.find()) {
            do {
                String[] numberTextPair = NUM_TEXT_SPLITTER_PATTERN.split(matcher.group());
                long timeAmount = Long.parseLong(numberTextPair[0]);
                String timeUnitIdentifier = numberTextPair[1].toLowerCase();

                int timeAmountScale;
                if (this.durationFormatConfig.getIdentifiers(TimeUnit.DAYS).contains(timeUnitIdentifier)) {
                    timeAmountScale = DAY_TO_SECONDS;
                } else if (this.durationFormatConfig.getIdentifiers(TimeUnit.HOURS).contains(timeUnitIdentifier)) {
                    timeAmountScale = HOUR_TO_SECONDS;
                } else if (this.durationFormatConfig.getIdentifiers(TimeUnit.MINUTES).contains(timeUnitIdentifier)) {
                    timeAmountScale = MINUTE_TO_SECONDS;
                } else if (this.durationFormatConfig.getIdentifiers(TimeUnit.SECONDS).contains(timeUnitIdentifier)) {
                    timeAmountScale = 1;
                } else {
                    throw new IllegalArgumentException("time unit %s is not supported".formatted(timeUnitIdentifier));
                }

                seconds = Math.addExact(seconds, Math.multiplyExact(timeAmount, timeAmountScale));
            } while (matcher.find());
        } else {
            throw new IllegalArgumentException("input could not be parsed");
        }

        return Duration.ofSeconds(seconds);
    }

    @Override
    public @NotNull String format(@NotNull Duration duration) {
        Preconditions.checkArgument(duration != null, "duration");
        if (duration.isZero() || duration.isNegative()) {
            return "0 " + this.durationFormatConfig.getPluralForm(TimeUnit.SECONDS);
        }

        StringBuilder formatBuilder = new StringBuilder();
        appendTimeUnit(formatBuilder, duration.toDaysPart(), TimeUnit.DAYS);
        appendTimeUnit(formatBuilder, duration.toHoursPart(), TimeUnit.HOURS);
        appendTimeUnit(formatBuilder, duration.toMinutesPart(), TimeUnit.MINUTES);
        appendTimeUnit(formatBuilder, duration.toSecondsPart(), TimeUnit.SECONDS);
        return formatBuilder.toString().trim();
    }

    private void appendTimeUnit(StringBuilder stringBuilder, long time, TimeUnit timeUnit) {
        if (time > 0) {
            stringBuilder.append(" ").append(time).append(" ");
            if (time > 1) {
                stringBuilder.append(this.durationFormatConfig.getPluralForm(timeUnit));
            } else {
                stringBuilder.append(this.durationFormatConfig.getSingularForm(timeUnit));
            }
        }
    }
}
