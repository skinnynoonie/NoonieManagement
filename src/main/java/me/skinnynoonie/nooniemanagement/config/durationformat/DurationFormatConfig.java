package me.skinnynoonie.nooniemanagement.config.durationformat;

import me.skinnynoonie.nooniemanagement.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface DurationFormatConfig extends Config {
    @NotNull String getPluralForm(TimeUnit timeUnit);

    @NotNull String getSingularForm(TimeUnit timeUnit);

    @NotNull Set<@NotNull String> getIdentifiers(TimeUnit timeUnit);
}
