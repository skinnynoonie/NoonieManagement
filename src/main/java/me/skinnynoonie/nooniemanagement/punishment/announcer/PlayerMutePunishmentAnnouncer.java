package me.skinnynoonie.nooniemanagement.punishment.announcer;

import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.config.ConfigManager;
import me.skinnynoonie.nooniemanagement.config.message.MessageConfig;
import me.skinnynoonie.nooniemanagement.config.permission.PermissionConfig;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.duration.DurationParser;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.util.PlayerUtil;
import me.skinnynoonie.nooniemanagement.util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class PlayerMutePunishmentAnnouncer implements PunishmentAnnouncer {
    private final ConfigManager configManager;
    private final DurationParser durationParser;

    public PlayerMutePunishmentAnnouncer(NoonieManagement noonieManagement) {
        this.configManager = noonieManagement.getConfigManager();
        this.durationParser = noonieManagement.getDurationParser();
    }

    @Override
    public void announceIssued(@NotNull Saved<? extends Punishment> savedPunishment) {
        if (savedPunishment.get() instanceof PlayerMutePunishment playerMute) {
            MessageConfig messageConfig = this.configManager.getMessageConfig();
            PermissionConfig permissionConfig = this.configManager.getPermissionConfig();

            String message;
            String target = PlayerUtil.getOfflineName(playerMute.getTarget());
            String issuer = PlayerUtil.getOfflineName(playerMute.getIssuer());
            if (playerMute.isPermanent()) {
                message = messageConfig.getPermanentPlayerMuteMessage(target, issuer);
            } else {
                String duration = this.durationParser.format(playerMute.getDuration());
                message = messageConfig.getPlayerMuteMessage(target, issuer, duration);
            }

            Component messageComponent = TextUtil.text(message);
            Bukkit.broadcast(messageComponent, permissionConfig.getPlayerMutesAnnouncePermission());
            Bukkit.getConsoleSender().sendMessage(messageComponent);
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
