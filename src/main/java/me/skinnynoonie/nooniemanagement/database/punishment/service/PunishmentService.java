package me.skinnynoonie.nooniemanagement.database.punishment.service;

import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerPunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.database.Saved;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PunishmentService {
    void init();

    void shutdown();

    @NotNull CompletableFuture<@NotNull PlayerPunishmentHistory> getPlayerHistory(@NotNull UUID target);

    @NotNull CompletableFuture<@NotNull PlayerMutePunishmentHistory> getPlayerMuteHistory(@NotNull UUID target);

    @NotNull CompletableFuture<@NotNull Saved<PlayerMutePunishment>> savePlayerMute(@NotNull PlayerMutePunishment mute);

    @NotNull CompletableFuture<Void> savePlayerMute(@NotNull Saved<PlayerMutePunishment> savedMute);
}
