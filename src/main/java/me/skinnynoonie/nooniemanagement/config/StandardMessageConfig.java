package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardMessageConfig extends AbstractStandardConfig implements MessageConfig {
    public StandardMessageConfig(ConfigurationSection config) {
        super(config);
    }

    @Override
    public @NotNull String getPlayerMuteMessage(@NotNull String target, @NotNull String issuer, @NotNull String duration) {
        Preconditions.checkArgument(target != null, "target");
        Preconditions.checkArgument(issuer != null, "issuer");
        Preconditions.checkArgument(duration != null, "duration");

        return super.config.getString("messages.player-mute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer)
                .replace("{duration}", duration);
    }

    @Override
    public @NotNull String getPermanentPlayerMuteMessage(@NotNull String target, @NotNull String issuer) {
        Preconditions.checkArgument(target != null, "target");
        Preconditions.checkArgument(issuer != null, "issuer");

        return super.config.getString("messages.player-permanent-mute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }

    @Override
    public @NotNull String getPlayerUnMuteMessage(@NotNull String target, @NotNull String issuer) {
        Preconditions.checkArgument(target != null, "target");
        Preconditions.checkArgument(issuer != null, "issuer");

        return super.config.getString("messages.player-unmute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }
}
