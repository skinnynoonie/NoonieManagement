package me.skinnynoonie.nooniemanagement.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Duration {

    /**
     * An infinite duration.
     */
    public static final Duration INFINITE = new Duration(-1, true);

    private static final Pattern timePattern = Pattern.compile("(\\d+(second|sec|s))|(\\d+(minute|min|m))|(\\d+(hour|hr|h))|(\\d+(day|d))");

    /**
     * Parse duration from milliseconds. Values cannot be less than 0.
     * @param millis Number of milliseconds.
     * @return A duration of a number of milliseconds.
     */
    @Nonnull
    public static Duration from(@Range(to = 0, from = Long.MAX_VALUE) long millis) {
        return new Duration(millis, false);
    }

    /**
     * Parse a duration from a String.
     * @param content The String to parse.
     * @return Duration of the String, or 0 if nothing could be parsed.
     */
    @Nonnull
    public static Duration parse(@Nonnull String content) {
        Preconditions.checkNotNull(content, "content to parse cannot be null!");

        long millis = 0;
        Matcher matcher = timePattern.matcher(content);

        while(matcher.find()) {

            String match = matcher.group();

            final long timeAmount = Long.parseLong(match.replaceAll("[a-zA-Z]+", ""));
            final String timeScale = match.replaceAll("\\d+", "");

            switch (timeScale) {
                case "s", "sec", "second" -> millis += timeAmount * 1000;

                case "m", "min", "minute" -> millis += timeAmount * 1000 * 60;

                case "h", "hr", "hour" -> millis += timeAmount * 1000 * 60 * 60;

                case "d", "day" -> millis += timeAmount * 1000 * 60 * 60 * 24;
            }

        }
        return new Duration(millis, false);
    }

    private final long millis;

    private Duration(long millis, boolean bypass) {
        if(millis < 0 && !bypass) {
            throw new IllegalStateException("Cannot have a duration less than 0.");
        }
        this.millis = millis;
    }

    /**
     * @return The milliseconds for this duration.
     * @throws java.lang.IllegalCallerException if this is an INFINITE duration.
     */
    public long getMillis() {
        if(millis < 0) {
            throw new IllegalCallerException("Cannot get millis for an INFINITE duration.");
        }
        return millis;
    }

    /**
     * @return true if this duration is infinite, otherwise false.
     */
    public boolean isInfinite() {
        return millis < 0;
    }

    /**
     * Formats this duration into a readable form.
     * @param infiniteFormat The format to be used for an infinite duration.
     * @return The formatted result.
     */
    @Nonnull
    public String getFormatted(String infiniteFormat) {
        if(isInfinite()) {
            return infiniteFormat;
        }

        long currentMillis = this.millis;

        long days = Math.floorDiv(currentMillis, 24 * 60 * 60 * 1000);
        currentMillis -= days * 24 * 60 * 60 * 1000;

        long hours = Math.floorDiv(currentMillis, 60 * 60 * 1000);
        currentMillis -= hours * 60 * 60 * 1000;

        long minutes = Math.floorDiv(currentMillis, 60 * 1000);
        currentMillis -= minutes * 60 * 1000;

        long seconds = Math.floorDiv(currentMillis, 1000);

        String format = "";
        if(days > 0) {
            format += (days + " days ");
        }
        if(hours > 0) {
            format += (hours + " hours ");
        }
        if(minutes > 0) {
            format += (minutes + " minutes ");
        }
        if(seconds > 0) {
            format += (seconds + " seconds ");
        }

        format = format.replaceAll(" $", "");
        if(format.matches(" *")) {
            format = "0 seconds";
        }

        return format;
    }

}
