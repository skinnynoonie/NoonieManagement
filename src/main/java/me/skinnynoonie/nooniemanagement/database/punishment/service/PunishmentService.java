package me.skinnynoonie.nooniemanagement.database.punishment.service;

import me.skinnynoonie.nooniemanagement.punishment.history.PlayerPunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.database.Saved;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PunishmentService {
    void init();

    void shutdown();

    CompletableFuture<PlayerPunishmentHistory> getPlayerHistory(UUID target);

    CompletableFuture<Saved<PlayerMutePunishment>> savePlayerMute(PlayerMutePunishment mute);

    CompletableFuture<Void> savePlayerMute(Saved<PlayerMutePunishment> savedMute);
}
