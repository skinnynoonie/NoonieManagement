package me.skinnynoonie.nooniemanagement.database.punishment;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerPunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AsyncPunishmentService {
    private final PunishmentService service;
    private final ExecutorService executor;

    public AsyncPunishmentService(PunishmentService service) {
        Preconditions.checkArgument(service != null, "service");

        this.service = service;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public void init() {
        this.service.init();
    }

    public void shutdown() {
        this.executor.shutdown();
        this.service.shutdown();
    }

    public CompletableFuture<PlayerPunishmentHistory> getPlayerHistory(@NotNull UUID target) {
        return CompletableFuture.supplyAsync(() -> this.service.getPlayerHistory(target), this.executor);
    }

    public CompletableFuture<PlayerMutePunishmentHistory> getPlayerMuteHistory(@NotNull UUID target) {
        return CompletableFuture.supplyAsync(() -> this.service.getPlayerMuteHistory(target), this.executor);
    }

    public CompletableFuture<Saved<PlayerMutePunishment>> savePlayerMute(@NotNull PlayerMutePunishment mute) {
        return CompletableFuture.supplyAsync(() -> this.service.savePlayerMute(mute), this.executor);
    }

    public CompletableFuture<Void> savePlayerMute(@NotNull Saved<PlayerMutePunishment> savedMute) {
        return CompletableFuture.runAsync(() -> this.service.savePlayerMute(savedMute), this.executor);
    }
}
