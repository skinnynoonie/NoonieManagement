package me.skinnynoonie.nooniemanagement.punishment.history;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.punishment.player.PlayerMutePunishment;
import me.skinnynoonie.nooniemanagement.database.Saved;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PlayerMutePunishmentHistory {
    private final List<Saved<PlayerMutePunishment>> mutes;

    public PlayerMutePunishmentHistory(@NotNull List<@NotNull Saved<PlayerMutePunishment>> mutes) {
        Preconditions.checkArgument(mutes != null, "mutes");
        Preconditions.checkArgument(!mutes.contains(null), "mutes contains null");

        this.mutes = mutes;
    }

    public @NotNull List<@NotNull Saved<PlayerMutePunishment>> getPunishments() {
        return Collections.unmodifiableList(this.mutes);
    }

    public @NotNull Map<Integer, PlayerMutePunishment> getPunishmentsMap() {
        Map<Integer, PlayerMutePunishment> punishmentMap = new HashMap<>();
        for (Saved<PlayerMutePunishment> savedMute : this.mutes) {
            punishmentMap.put(savedMute.getId(), savedMute.get());
        }
        return punishmentMap;
    }

    public @Nullable Saved<PlayerMutePunishment> getActiveMute() {
        for (Saved<PlayerMutePunishment> mute : this.mutes) {
            if (mute.get().isActive()) {
                return mute;
            }
        }
        return null;
    }
}
