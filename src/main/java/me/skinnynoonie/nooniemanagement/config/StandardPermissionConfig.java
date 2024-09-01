package me.skinnynoonie.nooniemanagement.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class StandardPermissionConfig extends AbstractStandardConfig implements PermissionConfig {
    public StandardPermissionConfig(@NotNull ConfigurationSection config) {
        super(config);
    }

    @Override
    public @NotNull String getPlayerMutesAnnouncePermission() {
        return super.config.getString("permissions.announces.view-player-mute-announces", "");
    }
}
