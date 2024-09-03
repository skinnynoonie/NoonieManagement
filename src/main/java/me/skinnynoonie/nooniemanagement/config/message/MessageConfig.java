package me.skinnynoonie.nooniemanagement.config.message;

import me.skinnynoonie.nooniemanagement.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MessageConfig extends Config {
    @NotNull String getConsoleName();

    @NotNull String getPlayerMuteMessage(@NotNull String target, @Nullable String issuer, @NotNull String duration);

    @NotNull String getPermanentPlayerMuteMessage(@NotNull String target, @Nullable String issuer);

    @NotNull String getPlayerUnMuteMessage(@NotNull String target, @Nullable String issuer);

    @NotNull String getPlayerAlreadyMutedMessage(@NotNull String target);

    @NotNull String getPlayerNotMutedMessage(@NotNull String target);

    @Override
    default boolean isValid() {
        return true;
    }
}
