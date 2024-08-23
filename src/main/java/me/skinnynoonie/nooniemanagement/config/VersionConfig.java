package me.skinnynoonie.nooniemanagement.config;

import org.jetbrains.annotations.NotNull;

public interface VersionConfig extends Config {
    String VERSION = "0.0.1";

    @NotNull String getVersion();

    default boolean isOutdated() {
        return !VERSION.equals(this.getVersion());
    }

    @Override
    default boolean isValid() {
        return true;
    }
}
