package me.skinnynoonie.nooniemanagement.database.connection;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public final class ConnectionProviderOptions {
    public static @NotNull ConnectionProviderOptions fromConfig(@NotNull DatabaseConfig config) {
        Preconditions.checkArgument(config != null, "config");

        return new ConnectionProviderOptions()
                .setHost(config.getHost())
                .setPort(config.getPort())
                .setDatabaseName(config.getName())
                .setUsername(config.getUsername())
                .setPassword(config.getPassword());
    }

    public static @NotNull ConnectionProviderOptions fromProperties(@NotNull Properties properties) {
        Preconditions.checkArgument(properties != null, "properties");

        return new ConnectionProviderOptions()
                .setHost(properties.getProperty("database.host"))
                .setPort(properties.getProperty("database.port"))
                .setDatabaseName(properties.getProperty("database.name"))
                .setUsername(properties.getProperty("database.username"))
                .setPassword(properties.getProperty("database.password"));
    }

    private String host;
    private String port;
    private String databaseName;
    private String username;
    private String password;

    public ConnectionProviderOptions() {
    }

    public @Nullable String getHost() {
        return this.host;
    }

    public @NotNull ConnectionProviderOptions setHost(@Nullable String host) {
        this.host = host;
        return this;
    }

    public @Nullable String getPort() {
        return this.port;
    }

    public @NotNull ConnectionProviderOptions setPort(@Nullable String port) {
        this.port = port;
        return this;
    }

    public @Nullable String getDatabaseName() {
        return this.databaseName;
    }

    public @NotNull ConnectionProviderOptions setDatabaseName(@Nullable String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public @Nullable String getUsername() {
        return this.username;
    }

    public @NotNull ConnectionProviderOptions setUsername(@Nullable String username) {
        this.username = username;
        return this;
    }

    public @Nullable String getPassword() {
        return this.password;
    }

    public @NotNull ConnectionProviderOptions setPassword(@Nullable String password) {
        this.password = password;
        return this;
    }
}
