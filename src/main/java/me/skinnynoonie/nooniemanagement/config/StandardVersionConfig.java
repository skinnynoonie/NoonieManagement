package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardVersionConfig implements VersionConfig {
    private final ConfigurationSection config;

    public StandardVersionConfig(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        this.config = config;
    }

    @Override
    public @NotNull String getVersion() {
        return this.config.getString("version", "");
    }
}
