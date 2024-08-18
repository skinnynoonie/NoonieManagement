package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardDatabaseConfig implements DatabaseConfig {
    private final ConfigurationSection config;

    public StandardDatabaseConfig(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        this.config = config;
    }

    @Override
    public boolean isValid() {
        return this.getDatabaseType() != null;
    }

    @Override
    public @NotNull String getDatabaseType() {
        return Preconditions.checkNotNull(this.config.getString("database.type"), "not using config properly, always use isValid() first");
    }

    @Override
    public @Nullable String getHost() {
        return this.config.getString("database.host");
    }

    @Override
    public @Nullable String getPort() {
        return this.config.getString("database.port");
    }

    @Override
    public @Nullable String getName() {
        return this.config.getString("database.name");
    }

    @Override
    public @Nullable String getUsername() {
        return this.config.getString("database.username");
    }

    @Override
    public @Nullable String getPassword() {
        return this.config.getString("database.password");
    }
}
