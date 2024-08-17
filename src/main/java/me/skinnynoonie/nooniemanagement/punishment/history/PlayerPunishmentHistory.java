package me.skinnynoonie.nooniemanagement.punishment.history;

public class PlayerPunishmentHistory {
    private final PlayerMutePunishmentHistory muteHistory;

    public PlayerPunishmentHistory(PlayerMutePunishmentHistory muteHistory) {
        this.muteHistory = muteHistory;
    }

    public PlayerMutePunishmentHistory getMuteHistory() {
        return this.muteHistory;
    }
}
