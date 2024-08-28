package me.skinnynoonie.nooniemanagement.database.punishment;

import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.Saved;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface PlayerMutePunishmentRepository {
    @Nullable Saved<PlayerMutePunishment> findById(int id) throws DatabaseException;

    @NotNull List<@NotNull Saved<PlayerMutePunishment>> findByTarget(@NotNull UUID target) throws DatabaseException;

    @NotNull Saved<PlayerMutePunishment> save(@NotNull PlayerMutePunishment mute) throws DatabaseException;

    void save(@NotNull Saved<PlayerMutePunishment> savedMute) throws DatabaseException;
}
