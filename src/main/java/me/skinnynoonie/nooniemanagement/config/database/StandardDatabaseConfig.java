package me.skinnynoonie.nooniemanagement.config.database;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardDatabaseConfig implements DatabaseConfig {
    public static StandardDatabaseConfig from(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        return new StandardDatabaseConfig(
                config.getString("database.type"),
                config.getString("database.host"),
                config.getString("database.port"),
                config.getString("database.name"),
                config.getString("database.username"),
                config.getString("database.password")
        );
    }

    private final String type;
    private final String host;
    private final String port;
    private final String name;
    private final String username;
    private final String password;

    public StandardDatabaseConfig(
            @NotNull String type,
            @Nullable String host,
            @Nullable String port,
            @Nullable String name,
            @Nullable String username,
            @Nullable String password
    ) {
        Preconditions.checkArgument(type != null, "type");

        this.type = type;
        this.host = host;
        this.port = port;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Override
    public @NotNull String getDatabaseType() {
        return this.type;
    }

    @Override
    public @Nullable String getPort() {
        return this.port;
    }

    @Override
    public @Nullable String getHost() {
        return this.host;
    }

    @Override
    public @Nullable String getName() {
        return this.name;
    }

    @Override
    public @Nullable String getUsername() {
        return this.username;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }
}
