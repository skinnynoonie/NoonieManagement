package me.skinnynoonie.nooniemanagement.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardMessageConfig implements MessageConfig {
    private final ConfigurationSection config;

    public StandardMessageConfig(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public @NotNull String getPlayerMuteMessage(@NotNull String target, @NotNull String issuer, @NotNull String duration) {
        return this.config.getString("messages.player-mute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer)
                .replace("{duration}", duration);
    }

    @Override
    public @NotNull String getPermanentPlayerMuteMessage(@NotNull String target, @NotNull String issuer) {
        return this.config.getString("messages.player-permanent-mute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }

    @Override
    public @NotNull String getPlayerUnMuteMessage(@NotNull String target, @NotNull String issuer) {
        return this.config.getString("messages.player-unmute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }
}
