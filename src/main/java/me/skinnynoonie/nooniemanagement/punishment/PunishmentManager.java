package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.punishment.service.PunishmentService;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PunishmentManager {
    private final NoonieManagement noonieManagement;

    public PunishmentManager(NoonieManagement noonieManagement) {
        this.noonieManagement = noonieManagement;
    }

    public @NotNull CompletableFuture<@NotNull Saved<PlayerMutePunishment>> mutePlayer(
            @NotNull UUID target, @Nullable UUID issuer, @Nullable String reason, @NotNull Duration duration
    ) {
        Preconditions.checkArgument(target != null, "target");
        Preconditions.checkArgument(duration != null, "duration");

        PunishmentService punishmentService = this.noonieManagement.getDatabaseManager().getPunishmentService();
        return punishmentService.getPlayerMuteHistory(target)
                .thenAccept(history -> {
                    if (history.getActiveMute() != null) {
                        throw new PunishmentException(PunishmentException.Reason.ALREADY_PUNISHED);
                    }
                })
                .thenCompose(ignored -> {
                    PlayerMutePunishment mute = new PlayerMutePunishment(target, issuer, reason, duration.getMillis());
                    return punishmentService.savePlayerMute(mute);
                });
    }

    public @NotNull CompletableFuture<@NotNull Saved<PlayerMutePunishment>> unMutePlayer(
            @NotNull UUID target, @Nullable UUID pardoner, @Nullable String reason
    ) {
        Preconditions.checkArgument(target != null, "target");

        PunishmentService punishmentService = this.noonieManagement.getDatabaseManager().getPunishmentService();
        return punishmentService.getPlayerMuteHistory(target)
                .thenApply(history -> {
                    Saved<PlayerMutePunishment> activeSavedMute = history.getActiveMute();
                    if (activeSavedMute == null) {
                        throw new PunishmentException(PunishmentException.Reason.NOT_PUNISHED);
                    } else {
                        return activeSavedMute;
                    }
                })
                .thenApply(activeSavedMute -> {
                    activeSavedMute.get().pardon(pardoner, reason);
                    punishmentService.savePlayerMute(activeSavedMute);
                    return activeSavedMute;
                });
    }

    public CompletableFuture<@NotNull PlayerMutePunishmentHistory> getPlayerMuteHistory(@NotNull  UUID target) {
        Preconditions.checkArgument(target != null, "target");

        return this.noonieManagement.getPunishmentManager().getPlayerMuteHistory(target);
    }
}
