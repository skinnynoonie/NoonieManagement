package me.skinnynoonie.nooniemanagement.config;

import org.jetbrains.annotations.NotNull;

public interface PermissionConfig extends Config {
    @NotNull String getPlayerMutesAnnouncePermission();

    @Override
    default boolean isValid() {
        return true;
    }
}
