package me.skinnynoonie.nooniemanagement.config.database;

import me.skinnynoonie.nooniemanagement.config.AbstractStandardConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardDatabaseConfig extends AbstractStandardConfig implements DatabaseConfig {
    public StandardDatabaseConfig(@NotNull ConfigurationSection config) {
        super(config);
    }

    @Override
    public @NotNull String getDatabaseType() {
        return super.config.getString("database.type", "");
    }

    @Override
    public @NotNull String getHost() {
        return super.config.getString("database.host", "");
    }

    @Override
    public @NotNull String getPort() {
        return super.config.getString("database.port", "");
    }

    @Override
    public @NotNull String getName() {
        return super.config.getString("database.name", "");
    }

    @Override
    public @NotNull String getUsername() {
        return super.config.getString("database.username", "");
    }

    @Override
    public @NotNull String getPassword() {
        return super.config.getString("database.password", "");
    }
}
