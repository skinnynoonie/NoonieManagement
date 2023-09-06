package me.skinnynoonie.nooniemanagement.punishment;

public enum PunishmentType {

    BLACKLIST(true),
    BAN(true),
    MUTE(true),
    WARN(false),
    KICK(false);

    private final boolean durational;

    PunishmentType(boolean durational) {
        this.durational = durational;
    }

    public boolean isDurational() {
        return this.durational;
    }
}
