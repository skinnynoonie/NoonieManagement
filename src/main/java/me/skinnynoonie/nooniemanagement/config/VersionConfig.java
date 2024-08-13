package me.skinnynoonie.nooniemanagement.config;

public interface VersionConfig extends Config {
    String VERSION = "0.0.1";

    String getVersion();

    default boolean isOutdated() {
        return !VERSION.equals(this.getVersion());
    }
}
