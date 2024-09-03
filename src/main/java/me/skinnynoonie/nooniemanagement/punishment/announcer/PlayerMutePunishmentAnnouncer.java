package me.skinnynoonie.nooniemanagement.punishment.announcer;

import me.skinnynoonie.nooniemanagement.config.ConfigManager;
import me.skinnynoonie.nooniemanagement.config.message.MessageConfig;
import me.skinnynoonie.nooniemanagement.config.permission.PermissionConfig;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.util.DurationUtil;
import me.skinnynoonie.nooniemanagement.util.PlayerUtil;
import me.skinnynoonie.nooniemanagement.util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class PlayerMutePunishmentAnnouncer implements PunishmentAnnouncer {
    private final ConfigManager configManager;

    public PlayerMutePunishmentAnnouncer(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void announceIssued(@NotNull Saved<? extends Punishment> savedPunishment) {
        if (savedPunishment.get() instanceof PlayerMutePunishment playerMute) {
            MessageConfig messageConfig = this.configManager.getMessageConfig();
            PermissionConfig permissionConfig = this.configManager.getPermissionConfig();

            String target = PlayerUtil.getOfflineName(playerMute.getTarget());
            String issuer = PlayerUtil.getOfflineName(playerMute.getIssuer());
            String duration = DurationUtil.format(playerMute.getDuration());
            Component message = TextUtil.text(playerMute.isPermanent() ?
                    messageConfig.getPermanentPlayerMuteMessage(target, issuer) :
                    messageConfig.getPlayerMuteMessage(target, issuer, duration)
            );

            Bukkit.broadcast(message, permissionConfig.getPlayerMutesAnnouncePermission());
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

    @Override
    public void announcePardoned(@NotNull Saved<? extends Punishment> savedPunishment) {
        if (savedPunishment.get() instanceof PlayerMutePunishment playerMute) {
            MessageConfig messageConfig = this.configManager.getMessageConfig();
            PermissionConfig permissionConfig = this.configManager.getPermissionConfig();

            String target = PlayerUtil.getOfflineName(playerMute.getTarget());
            String issuer = PlayerUtil.getOfflineName(playerMute.getIssuer());
            Component message = TextUtil.text(messageConfig.getPlayerUnMuteMessage(target, issuer));

            Bukkit.broadcast(message, permissionConfig.getPlayerMutesAnnouncePermission());
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }
}
