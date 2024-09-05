package me.skinnynoonie.nooniemanagement.config.permission;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardPermissionConfig implements PermissionConfig {
    public static StandardPermissionConfig from(@NotNull ConfigurationSection config) {
        Preconditions.checkArgument(config != null, "config");

        return new StandardPermissionConfig(
                config.getString("permissions.announces.view-player-mute-announces"),
                config.getString("permissions.commands.player-mute-command"),
                config.getString("permissions.commands.player-unmute-command")
        );
    }

    private final String playerMutesAnnouncePermission;
    private final String playerMuteCommandPermission;
    private final String playerUnMuteCommandPermission;

    public StandardPermissionConfig(
            @NotNull String playerMutesAnnouncePermission,
            @NotNull String playerMuteCommandPermission,
            @NotNull String playerUnMuteCommandPermission
    ) {
        Preconditions.checkArgument(playerMutesAnnouncePermission != null, "playerMutesAnnouncePermission");
        Preconditions.checkArgument(playerMuteCommandPermission != null, "playerMuteCommandPermission");
        Preconditions.checkArgument(playerUnMuteCommandPermission != null, "playerUnMuteCommandPermission");

        this.playerMutesAnnouncePermission = playerMutesAnnouncePermission;
        this.playerMuteCommandPermission = playerMuteCommandPermission;
        this.playerUnMuteCommandPermission = playerUnMuteCommandPermission;
    }

    @Override
    public @NotNull String getPlayerMutesAnnouncePermission() {
        return this.playerMutesAnnouncePermission;
    }

    @Override
    public @NotNull String getPlayerMuteCommandPermission() {
        return this.playerMuteCommandPermission;
    }

    @Override
    public @NotNull String getPlayerUnMuteCommandPermission() {
        return this.playerUnMuteCommandPermission;
    }
}
