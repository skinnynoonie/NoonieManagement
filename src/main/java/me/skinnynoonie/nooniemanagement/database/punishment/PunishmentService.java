package me.skinnynoonie.nooniemanagement.database.punishment;

import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerPunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PunishmentService {
    void init() throws DatabaseException;

    void shutdown() throws DatabaseException;

    @NotNull PlayerPunishmentHistory getPlayerHistory(@NotNull UUID target) throws DatabaseException;

    @NotNull PlayerMutePunishmentHistory getPlayerMuteHistory(@NotNull UUID target) throws DatabaseException;

    @NotNull Saved<PlayerMutePunishment> savePlayerMute(@NotNull PlayerMutePunishment mute) throws DatabaseException;

    void savePlayerMute(@NotNull Saved<PlayerMutePunishment> savedMute) throws DatabaseException;
}
