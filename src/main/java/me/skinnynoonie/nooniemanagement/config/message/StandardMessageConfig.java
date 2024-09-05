package me.skinnynoonie.nooniemanagement.config.message;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class StandardMessageConfig implements MessageConfig {
    public static StandardMessageConfig from(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        System.out.println(config.getString("messages.mute.player-unmute"));
        return new StandardMessageConfig(
                config.getString("messages.console-name"),
                config.getString("messages.mute.player-mute"),
                config.getString("messages.mute.player-permanent-mute"),
                config.getString("messages.mute.player-unmute"),
                config.getString("messages.mute.player-already-muted"),
                config.getString("messages.mute.player-not-muted")
        );
    }

    private final String consoleName;
    private final String playerMuteMessage;
    private final String permanentPlayerMuteMessage;
    private final String playerUnMuteMessage;
    private final String playerAlreadyMutedMessage;
    private final String playerNotMutedMessage;

    public StandardMessageConfig(
            @NotNull String consoleName,
            @NotNull String playerMuteMessage,
            @NotNull String permanentPlayerMuteMessage,
            @NotNull String playerUnMuteMessage,
            @NotNull String playerAlreadyMutedMessage,
            @NotNull String playerNotMutedMessage
    ) {
        Preconditions.checkArgument(consoleName != null, "consoleName");
        Preconditions.checkArgument(playerMuteMessage != null, "playerMuteMessage");
        Preconditions.checkArgument(permanentPlayerMuteMessage != null, "permanentPlayerMuteMessage");
        Preconditions.checkArgument(playerUnMuteMessage != null, "playerUnMuteMessage");
        Preconditions.checkArgument(playerAlreadyMutedMessage != null, "playerAlreadyMutedMessage");
        Preconditions.checkArgument(playerNotMutedMessage != null, "playerNotMutedMessage");

        this.consoleName = consoleName;
        this.playerMuteMessage = playerMuteMessage;
        this.permanentPlayerMuteMessage = permanentPlayerMuteMessage;
        this.playerUnMuteMessage = playerUnMuteMessage;
        this.playerAlreadyMutedMessage = playerAlreadyMutedMessage;
        this.playerNotMutedMessage = playerNotMutedMessage;
    }

    @Override
    public @NotNull String getConsoleName() {
        return this.consoleName;
    }

    @Override
    public @NotNull String getPlayerMuteMessage(@NotNull String target, @Nullable String issuer, @NotNull String duration) {
        Preconditions.checkArgument(target != null, "target");
        issuer = Objects.requireNonNullElse(issuer, this.getConsoleName());
        Preconditions.checkArgument(duration != null, "duration");

        return this.playerMuteMessage
                .replace("{target}", target)
                .replace("{issuer}", issuer)
                .replace("{duration}", duration);
    }

    @Override
    public @NotNull String getPermanentPlayerMuteMessage(@NotNull String target, @Nullable String issuer) {
        Preconditions.checkArgument(target != null, "target");
        issuer = Objects.requireNonNullElse(issuer, this.getConsoleName());

        return this.permanentPlayerMuteMessage
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }

    @Override
    public @NotNull String getPlayerUnMuteMessage(@NotNull String target, @Nullable String issuer) {
        Preconditions.checkArgument(target != null, "target");
        issuer = Objects.requireNonNullElse(issuer, this.getConsoleName());

        return this.playerUnMuteMessage
                .replace("{target}", target)
                .replace("{issuer}", issuer);
    }

    @Override
    public @NotNull String getPlayerAlreadyMutedMessage(@NotNull String target) {
        Preconditions.checkArgument(target != null, "target");

        return this.playerAlreadyMutedMessage
                .replace("{target}", target);
    }

    @Override
    public @NotNull String getPlayerNotMutedMessage(@NotNull String target) {
        Preconditions.checkArgument(target != null, "target");

        return this.playerNotMutedMessage
                .replace("{target}", target);
    }
}
