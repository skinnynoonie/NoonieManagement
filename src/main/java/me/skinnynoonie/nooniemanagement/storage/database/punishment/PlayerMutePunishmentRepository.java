package me.skinnynoonie.nooniemanagement.storage.database.punishment;

import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.storage.database.DatabaseException;

import java.util.List;
import java.util.UUID;

public interface PlayerMutePunishmentRepository {
    void init() throws DatabaseException;

    SavedPunishment<PlayerMutePunishment> save(PlayerMutePunishment punishment) throws DatabaseException;

    void update(SavedPunishment<PlayerMutePunishment> punishment) throws DatabaseException;

    List<SavedPunishment<PlayerMutePunishment>> findByTarget(UUID target) throws DatabaseException;
}
