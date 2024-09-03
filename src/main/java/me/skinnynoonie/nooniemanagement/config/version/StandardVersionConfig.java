package me.skinnynoonie.nooniemanagement.config.version;

import me.skinnynoonie.nooniemanagement.config.AbstractStandardConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardVersionConfig extends AbstractStandardConfig implements VersionConfig {
    public StandardVersionConfig(@NotNull ConfigurationSection config) {
        super(config);
    }

    @Override
    public @NotNull String getVersion() {
        return super.config.getString("version", "");
    }
}