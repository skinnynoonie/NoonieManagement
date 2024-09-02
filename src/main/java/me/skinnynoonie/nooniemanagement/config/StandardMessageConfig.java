package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class StandardMessageConfig extends AbstractStandardConfig implements MessageConfig {
    public StandardMessageConfig(ConfigurationSection config) {
        super(config);
    }

    @Override
    public @NotNull String getConsoleName() {
        return super.config.getString("messages.console-name", "");
    }

    @Override
    public @NotNull String getPlayerMuteMessage(@NotNull String target, @Nullable String issuer, @NotNull String duration) {
        Preconditions.checkArgument(target != null, "target");
        issuer = Objects.requireNonNullElse(issuer, this.getConsoleName());
        Preconditions.checkArgument(duration != null, "duration");

        return super.config.getString("messages.mute.player-mute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer)
                .replace("{duration}", duration);
    }

    @Override
    public @NotNull String getPermanentPlayerMuteMessage(@NotNull String target, @Nullable String issuer) {
        Preconditions.checkArgument(target != null, "target");
        issuer = Objects.requireNonNullElse(issuer, this.getConsoleName());

        return super.config.getString("messages.mute.player-permanent-mute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }

    @Override
    public @NotNull String getPlayerUnMuteMessage(@NotNull String target, @Nullable String issuer) {
        Preconditions.checkArgument(target != null, "target");
        issuer = Objects.requireNonNullElse(issuer, this.getConsoleName());

        return super.config.getString("messages.mute.player-unmute", "")
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }

    @Override
    public @NotNull String getPlayerAlreadyMutedMessage(@NotNull String target) {
        Preconditions.checkArgument(target != null, "target");

        return super.config.getString("messages.mute.player-already-muted", "")
                .replace("{target}", target);
    }

    @Override
    public @NotNull String getPlayerNotMutedMessage(@NotNull String target) {
        Preconditions.checkArgument(target != null, "target");

        return super.config.getString("messages.mute.player-not-muted", "")
                .replace("{target}", target);
    }
}
