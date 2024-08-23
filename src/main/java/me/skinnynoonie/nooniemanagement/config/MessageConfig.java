package me.skinnynoonie.nooniemanagement.config;

import org.jetbrains.annotations.NotNull;

public interface MessageConfig extends Config {
    @NotNull String getPlayerMuteMessage(@NotNull String target, @NotNull String issuer, @NotNull String duration);

    @NotNull String getPermanentPlayerMuteMessage(@NotNull String target, @NotNull String issuer);

    @NotNull String getPlayerUnMuteMessage(@NotNull String target, @NotNull String issuer);

    @Override
    default boolean isValid() {
        return true;
    }
}
