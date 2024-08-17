package me.skinnynoonie.nooniemanagement.config;

import org.bukkit.configuration.ConfigurationSection;

public final class StandardDatabaseConfig implements DatabaseConfig {
    private final ConfigurationSection config;

    public StandardDatabaseConfig(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public boolean isValid() {
        return this.getDatabaseType() != null;
    }

    @Override
    public String getDatabaseType() {
        return this.config.getString("database.type");
    }

    @Override
    public String getHost() {
        return this.config.getString("database.host");
    }

    @Override
    public String getPort() {
        return this.config.getString("database.port");
    }

    @Override
    public String getName() {
        return this.config.getString("database.name");
    }

    @Override
    public String getUsername() {
        return this.config.getString("database.username");
    }

    @Override
    public String getPassword() {
        return this.config.getString("database.password");
    }
}
