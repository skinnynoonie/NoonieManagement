package me.skinnynoonie.nooniemanagement.config.database;

import me.skinnynoonie.nooniemanagement.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DatabaseConfig extends Config {
    @NotNull String getDatabaseType();

    @Nullable String getHost();

    @Nullable String getPort();

    @Nullable String getName();

    @Nullable String getUsername();

    @Nullable String getPassword();
}
