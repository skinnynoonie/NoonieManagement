package me.skinnynoonie.nooniemanagement.database.punishment.repository;

import me.skinnynoonie.nooniemanagement.database.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.punishment.SavedPunishment;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;

import java.util.List;
import java.util.UUID;

public interface PlayerMutePunishmentRepository {
    void init() throws DatabaseException;

    SavedPunishment<PlayerMutePunishment> save(PlayerMutePunishment punishment) throws DatabaseException;

    void update(SavedPunishment<PlayerMutePunishment> punishment) throws DatabaseException;

    List<SavedPunishment<PlayerMutePunishment>> findByTarget(UUID target) throws DatabaseException;
}
