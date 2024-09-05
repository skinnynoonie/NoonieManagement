package me.skinnynoonie.nooniemanagement.config.version;

import me.skinnynoonie.nooniemanagement.config.Config;
import org.jetbrains.annotations.NotNull;

public interface VersionConfig extends Config {
    String VERSION = "0.0.1";

    @NotNull String getVersion();

    default boolean isOutdated() {
        return !this.getVersion().equals(VERSION);
    }
}
