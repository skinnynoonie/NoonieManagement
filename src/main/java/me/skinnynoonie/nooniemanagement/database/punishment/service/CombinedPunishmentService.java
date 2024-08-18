package me.skinnynoonie.nooniemanagement.database.punishment.service;

import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.punishment.repository.PlayerMutePunishmentRepository;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerPunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public final class CombinedPunishmentService implements PunishmentService {
    private final PlayerMutePunishmentRepository playerMuteRepository;
    private final ExecutorService executor;

    public CombinedPunishmentService(PlayerMutePunishmentRepository playerMuteRepository) {
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
    public CompletableFuture<PlayerPunishmentHistory> getPlayerHistory(UUID target) {
        return this.supply(() -> new PlayerPunishmentHistory(
                new PlayerMutePunishmentHistory(this.playerMuteRepository.findByTarget(target))
        ));
    }

    @Override
    public CompletableFuture<PlayerMutePunishmentHistory> getPlayerMuteHistory(UUID target) {
        return this.supply(() -> new PlayerMutePunishmentHistory(
                this.playerMuteRepository.findByTarget(target)
        ));
    }

    @Override
    public CompletableFuture<Saved<PlayerMutePunishment>> savePlayerMute(PlayerMutePunishment mute) {
        return this.supply(() -> this.playerMuteRepository.save(mute));
    }

    @Override
    public CompletableFuture<Void> savePlayerMute(Saved<PlayerMutePunishment> savedMute) {
        return this.run(() -> this.playerMuteRepository.save(savedMute));
    }

    private <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, this.executor);
    }

    private CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, this.executor);
    }
}
