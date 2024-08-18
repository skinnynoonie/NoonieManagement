package me.skinnynoonie.nooniemanagement.punishment.history;

import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.database.Saved;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PlayerMutePunishmentHistory {
    private final List<Saved<PlayerMutePunishment>> mutes;

    public PlayerMutePunishmentHistory(List<Saved<PlayerMutePunishment>> mutes) {
        this.mutes = mutes;
    }

    public List<Saved<PlayerMutePunishment>> getPunishments() {
        return Collections.unmodifiableList(this.mutes);
    }

    public Map<Integer, PlayerMutePunishment> getPunishmentsMap() {
        Map<Integer, PlayerMutePunishment> punishmentMap = new HashMap<>();
        for (Saved<PlayerMutePunishment> savedMute : this.mutes) {
            punishmentMap.put(savedMute.getId(), savedMute.get());
        }
        return punishmentMap;
    }

    public Saved<PlayerMutePunishment> getActiveMute() {
        for (Saved<PlayerMutePunishment> mute : this.mutes) {
            if (mute.get().isActive()) {
                return mute;
            }
        }
        return null;
    }
}
