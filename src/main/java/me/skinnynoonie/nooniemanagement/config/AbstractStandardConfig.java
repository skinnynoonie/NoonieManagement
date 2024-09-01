package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractStandardConfig implements Config {
    protected final ConfigurationSection config;

    protected AbstractStandardConfig(ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        this.config = config;
    }
}
