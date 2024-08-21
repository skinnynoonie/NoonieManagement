package me.skinnynoonie.nooniemanagement.database.punishment.service;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.punishment.repository.PlayerMutePunishmentRepository;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerPunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public final class CombinedPunishmentService implements PunishmentService {
    private final PlayerMutePunishmentRepository playerMuteRepository;
    private final ExecutorService executor;

    public CombinedPunishmentService(
            @NotNull PlayerMutePunishmentRepository playerMuteRepository
    ) {
        Preconditions.checkArgument(playerMuteRepository != null, "playerMuteRepository");

        this.playerMuteRepository = playerMuteRepository;
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void init() {
        this.playerMuteRepository.init();
    }

    @Override
    public void shutdown() {
        this.executor.shutdown();
    }

    @Override
    public @NotNull CompletableFuture<@NotNull PlayerPunishmentHistory> getPlayerHistory(@NotNull UUID target) {
        Preconditions.checkArgument(target != null, "target");

        return this.supply(() -> new PlayerPunishmentHistory(
                new PlayerMutePunishmentHistory(this.playerMuteRepository.findByTarget(target))
        ));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull PlayerMutePunishmentHistory> getPlayerMuteHistory(@NotNull UUID target) {
        Preconditions.checkArgument(target != null, "target");

        return this.supply(() -> new PlayerMutePunishmentHistory(
                this.playerMuteRepository.findByTarget(target)
        ));
    }

    @Override
    public @NotNull CompletableFuture<@NotNull Saved<PlayerMutePunishment>> savePlayerMute(@NotNull PlayerMutePunishment mute) {
        Preconditions.checkArgument(mute != null, "mute");

        return this.supply(() -> this.playerMuteRepository.save(mute));
    }

    @Override
    public @NotNull CompletableFuture<Void> savePlayerMute(@NotNull Saved<PlayerMutePunishment> savedMute) {
        Preconditions.checkArgument(savedMute != null, "savedMute");

        return this.run(() -> this.playerMuteRepository.save(savedMute));
    }

    private <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, this.executor);
    }

    private CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, this.executor);
    }
}
