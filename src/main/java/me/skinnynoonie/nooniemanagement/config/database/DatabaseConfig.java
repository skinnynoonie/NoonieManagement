package me.skinnynoonie.nooniemanagement.config.database;

import me.skinnynoonie.nooniemanagement.config.Config;
import org.jetbrains.annotations.NotNull;

public interface DatabaseConfig extends Config {
    @NotNull String getDatabaseType();

    @NotNull String getHost();

    @NotNull String getPort();

    @NotNull String getName();

    @NotNull String getUsername();

    @NotNull String getPassword();

    @Override
    default boolean isValid() {
        return true;
    }
}
