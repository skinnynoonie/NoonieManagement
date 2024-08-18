package me.skinnynoonie.nooniemanagement.punishment;

import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.database.punishment.SavedPunishment;
import me.skinnynoonie.nooniemanagement.database.punishment.service.PunishmentService;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.util.Duration;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PunishmentManager {
    private final NoonieManagement noonieManagement;

    public PunishmentManager(NoonieManagement noonieManagement) {
        this.noonieManagement = noonieManagement;
    }

    public CompletableFuture<SavedPunishment<PlayerMutePunishment>> mutePlayer(
            UUID target, UUID issuer, String reason, Duration duration
    ) {
        PunishmentService punishmentService = this.noonieManagement.getDatabaseManager().getPunishmentService();
        return punishmentService.getPlayerHistory(target)
                .thenAccept(history -> {
                    if (history.getMuteHistory().getActiveMute() != null) {
                        throw new PunishmentException(PunishmentException.Reason.ALREADY_PUNISHED);
                    }
                })
                .thenCompose(ignored -> {
                    PlayerMutePunishment mute = new PlayerMutePunishment(target, issuer, reason, duration.getMillis());
                    return punishmentService.savePlayerMute(mute);
                });
    }

    public CompletableFuture<SavedPunishment<PlayerMutePunishment>> unMutePlayer(
            UUID target, UUID pardoner, String reason
    ) {
        PunishmentService punishmentService = this.noonieManagement.getDatabaseManager().getPunishmentService();
        return punishmentService.getPlayerHistory(target)
                .thenApply(history -> {
                    SavedPunishment<PlayerMutePunishment> activeSavedMute = history.getMuteHistory().getActiveMute();
                    if (activeSavedMute == null) {
                        throw new PunishmentException(PunishmentException.Reason.NOT_PUNISHED);
                    } else {
                        return activeSavedMute;
                    }
                })
                .thenApply(activeSavedMute -> {
                    activeSavedMute.getPunishment().pardon(pardoner, reason);
                    punishmentService.updatePlayerMute(activeSavedMute);
                    return activeSavedMute;
                });
    }
}
