package me.skinnynoonie.nooniemanagement.punishment.history;

import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.database.punishment.SavedPunishment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PlayerMutePunishmentHistory {
    private final List<SavedPunishment<PlayerMutePunishment>> mutes;

    public PlayerMutePunishmentHistory(List<SavedPunishment<PlayerMutePunishment>> mutes) {
        this.mutes = mutes;
    }

    public List<SavedPunishment<PlayerMutePunishment>> getPunishments() {
        return Collections.unmodifiableList(this.mutes);
    }

    public Map<Integer, PlayerMutePunishment> getPunishmentsMap() {
        Map<Integer, PlayerMutePunishment> punishmentMap = new HashMap<>();
        for (SavedPunishment<PlayerMutePunishment> savedPunishment : this.mutes) {
            punishmentMap.put(savedPunishment.getId(), savedPunishment.getPunishment());
        }
        return punishmentMap;
    }

    public SavedPunishment<PlayerMutePunishment> getActiveMute() {
        for (SavedPunishment<PlayerMutePunishment> mute : this.mutes) {
            if (mute.getPunishment().isActive()) {
                return mute;
            }
        }
        return null;
    }
}
