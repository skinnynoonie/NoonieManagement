package me.skinnynoonie.nooniemanagement.database.source;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DatabaseSourceOptions {
    public static @NotNull DatabaseSourceOptions fromConfig(@NotNull DatabaseConfig config) {
        Preconditions.checkArgument(config != null, "config");

        return new DatabaseSourceOptions()
                .setHost(config.getHost())
                .setPort(config.getPort())
                .setDatabaseName(config.getName())
                .setUsername(config.getUsername())
                .setPassword(config.getPassword());
    }

    private String username;
    private String password;
    private String host;
    private String port;
    private String databaseName;

    public DatabaseSourceOptions() {
    }

    public @Nullable String getUsername() {
        return this.username;
    }

    public @NotNull DatabaseSourceOptions setUsername(@Nullable String username) {
        this.username = username;
        return this;
    }

    public @Nullable String getPassword() {
        return this.password;
    }

    public @NotNull DatabaseSourceOptions setPassword(@Nullable String password) {
        this.password = password;
        return this;
    }

    public @Nullable String getHost() {
        return this.host;
    }

    public @NotNull DatabaseSourceOptions setHost(@Nullable String host) {
        this.host = host;
        return this;
    }

    public @Nullable String getPort() {
        return this.port;
    }

    public @NotNull DatabaseSourceOptions setPort(@Nullable String port) {
        this.port = port;
        return this;
    }

    public @Nullable String getDatabaseName() {
        return this.databaseName;
    }

    public @NotNull DatabaseSourceOptions setDatabaseName(@Nullable String databaseName) {
        this.databaseName = databaseName;
        return this;
    }
}
