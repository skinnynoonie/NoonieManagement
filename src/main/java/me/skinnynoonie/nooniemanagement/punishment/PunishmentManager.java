package me.skinnynoonie.nooniemanagement.punishment;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.database.DatabaseManager;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.punishment.AsyncPunishmentService;
import me.skinnynoonie.nooniemanagement.punishment.announcer.PunishmentAnnouncer;
import me.skinnynoonie.nooniemanagement.punishment.announcer.StandardPunishmentAnnouncer;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PunishmentManager {
    private final DatabaseManager databaseManager;
    private final PunishmentAnnouncer announcer;

    public PunishmentManager(NoonieManagement noonieManagement) {
        Preconditions.checkArgument(noonieManagement != null, "noonieManagement");

        this.databaseManager = noonieManagement.getDatabaseManager();
        this.announcer = new StandardPunishmentAnnouncer(noonieManagement);
    }

    public @NotNull CompletableFuture<@NotNull Saved<PlayerMutePunishment>> mutePlayer(
            @NotNull UUID target, @Nullable UUID issuer, @Nullable String reason, @NotNull Duration duration
    ) {
        Preconditions.checkArgument(target != null, "target");
        Preconditions.checkArgument(duration != null, "duration");

        AsyncPunishmentService asyncPunishmentService = this.databaseManager.getAsyncPunishmentService();
        return asyncPunishmentService.getPlayerMuteHistory(target)
                .thenAccept(history -> {
                    if (history.getActiveMute() != null) {
                        throw new PunishmentException(PunishmentException.Reason.ALREADY_PUNISHED);
                    }
                })
                .thenCompose(ignored -> {
                    PlayerMutePunishment mute = new PlayerMutePunishment(target, issuer, reason, duration);
                    return asyncPunishmentService.savePlayerMute(mute);
                })
                .thenApply(savedPlayerMute -> {
                    this.announcer.announceIssued(savedPlayerMute);
                    return savedPlayerMute;
                });
    }

    public @NotNull CompletableFuture<@NotNull Saved<PlayerMutePunishment>> unMutePlayer(
            @NotNull UUID target, @Nullable UUID pardoner, @Nullable String reason
    ) {
        Preconditions.checkArgument(target != null, "target");

        AsyncPunishmentService asyncPunishmentService = this.databaseManager.getAsyncPunishmentService();
        return asyncPunishmentService.getPlayerMuteHistory(target)
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
                    asyncPunishmentService.savePlayerMute(activeSavedMute).join();
                    return activeSavedMute;
                })
                .thenApply(pardonedSavedMute -> {
                    this.announcer.announcePardoned(pardonedSavedMute);
                    return pardonedSavedMute;
                });
    }

    public CompletableFuture<@NotNull PlayerMutePunishmentHistory> getPlayerMuteHistory(@NotNull  UUID target) {
        Preconditions.checkArgument(target != null, "target");

        return this.databaseManager.getAsyncPunishmentService().getPlayerMuteHistory(target);
    }
}
