package me.skinnynoonie.nooniemanagement.database.punishment;

import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.database.exception.DatabaseException;
import me.skinnynoonie.nooniemanagement.punishment.history.PlayerMutePunishmentHistory;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PunishmentService {
    void init() throws DatabaseException;

    void shutdown() throws DatabaseException;

    @Nullable Saved<PlayerMutePunishment> findPlayerMuteById(int id) throws DatabaseException;

    @NotNull PlayerMutePunishmentHistory getPlayerMuteHistory(@NotNull UUID target) throws DatabaseException;

    @NotNull Saved<PlayerMutePunishment> savePlayerMute(@NotNull PlayerMutePunishment mute) throws DatabaseException;

    void savePlayerMute(@NotNull Saved<PlayerMutePunishment> savedMute) throws DatabaseException;
}
