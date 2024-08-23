package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardDatabaseConfig implements DatabaseConfig {
    private final ConfigurationSection config;

    public StandardDatabaseConfig(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        this.config = config;
    }

    @Override
    public @NotNull String getDatabaseType() {
        return this.config.getString("database.type", "");
    }

    @Override
    public @NotNull String getHost() {
        return this.config.getString("database.host", "");
    }

    @Override
    public @NotNull String getPort() {
        return this.config.getString("database.port", "");
    }

    @Override
    public @NotNull String getName() {
        return this.config.getString("database.name", "");
    }

    @Override
    public @NotNull String getUsername() {
        return this.config.getString("database.username", "");
    }

    @Override
    public @NotNull String getPassword() {
        return this.config.getString("database.password", "");
    }
}
