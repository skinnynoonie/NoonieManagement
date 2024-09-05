package me.skinnynoonie.nooniemanagement.config.version;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardVersionConfig implements VersionConfig {
    public static StandardVersionConfig from(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        return new StandardVersionConfig(config.getString("version"));
    }

    private final String version;

    public StandardVersionConfig(@NotNull String version) {
        Preconditions.checkArgument(version != null, "version");

        this.version = version;
    }

    @Override
    public @NotNull String getVersion() {
        return this.version;
    }
}
