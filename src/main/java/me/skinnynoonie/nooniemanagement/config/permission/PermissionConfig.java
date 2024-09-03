package me.skinnynoonie.nooniemanagement.config.permission;

import me.skinnynoonie.nooniemanagement.config.Config;
import org.jetbrains.annotations.NotNull;

public interface PermissionConfig extends Config {
    @NotNull String getPlayerMutesAnnouncePermission();

    @NotNull String getPlayerMuteCommandPermission();

    @NotNull String getPlayerUnMuteCommandPermission();

    @Override
    default boolean isValid() {
        return true;
    }
}
