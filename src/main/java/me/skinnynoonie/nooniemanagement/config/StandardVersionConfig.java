package me.skinnynoonie.nooniemanagement.config;

import org.bukkit.configuration.ConfigurationSection;

public final class StandardVersionConfig implements VersionConfig {
    private final ConfigurationSection config;

    public StandardVersionConfig(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public boolean isValid() {
        return this.getVersion() != null;
    }

    @Override
    public String getVersion() {
        return this.config.getString("version");
    }
}
