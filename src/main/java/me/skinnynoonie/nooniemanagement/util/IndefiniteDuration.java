package me.skinnynoonie.nooniemanagement.util;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndefiniteDuration {

    public static final IndefiniteDuration INFINITE = new IndefiniteDuration(-1);

    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+(second|sec|s))|(\\d+(minute|min|m))|(\\d+(hour|hr|h))|(\\d+(day|d))");

    public static IndefiniteDuration from(long millis) {
        Preconditions.checkState(millis >= 0, "Durations cannot be less than 0.");
        return new IndefiniteDuration(millis);
    }

    public static IndefiniteDuration parse(String content) {
        Preconditions.checkNotNull(content, "content to parse cannot be null!");

        long millis = 0;
        Matcher matcher = TIME_PATTERN.matcher(content);

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
        return new IndefiniteDuration(millis);
    }

    private final long millis;

    private IndefiniteDuration(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        Preconditions.checkState(millis >= 0, "Cannot get millis for an INFINITE duration.");
        return millis;
    }

    public boolean isInfinite() {
        return millis < 0;
    }

    public String getFormatted(String infiniteFormat) {
        if(isInfinite()) return infiniteFormat;

        long currentMillis = this.millis;
        long days = Math.floorDiv(currentMillis, 24 * 60 * 60 * 1000); currentMillis -= days * 24 * 60 * 60 * 1000;
        long hours = Math.floorDiv(currentMillis, 60 * 60 * 1000); currentMillis -= hours * 60 * 60 * 1000;
        long minutes = Math.floorDiv(currentMillis, 60 * 1000); currentMillis -= minutes * 60 * 1000;
        long seconds = Math.floorDiv(currentMillis, 1000);

        String format = "";
        if(days > 0) format += (days + " days ");
        if(hours > 0) format += (hours + " hours ");
        if(minutes > 0) format += (minutes + " minutes ");
        if(seconds > 0) format += (seconds + " seconds ");

        format = format.replaceAll(" $", "");
        if(format.matches(" *")) format = "0 seconds";

        return format;
    }

}
