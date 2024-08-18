package me.skinnynoonie.nooniemanagement.punishment.history;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class PlayerPunishmentHistory {
    private final PlayerMutePunishmentHistory muteHistory;

    public PlayerPunishmentHistory(@NotNull PlayerMutePunishmentHistory muteHistory) {
        Preconditions.checkArgument(muteHistory != null, "muteHistory");

        this.muteHistory = muteHistory;
    }

    public @NotNull PlayerMutePunishmentHistory getMuteHistory() {
        return this.muteHistory;
    }
}
