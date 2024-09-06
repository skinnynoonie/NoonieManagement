package me.skinnynoonie.nooniemanagement.duration;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public interface DurationParser {
    @NotNull Duration parse(@NotNull String input);

    @NotNull String format(@NotNull Duration duration);
}
