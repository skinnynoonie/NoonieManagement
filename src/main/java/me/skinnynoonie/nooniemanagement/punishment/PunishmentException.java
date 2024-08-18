package me.skinnynoonie.nooniemanagement.punishment;

public final class PunishmentException extends RuntimeException {
    enum Reason {
        ALREADY_PUNISHED,
        NOT_PUNISHED;
    }

    private final Reason reason;

    public PunishmentException(Reason reason) {
        this.reason = reason;
    }

    public Reason getReason() {
        return this.reason;
    }
}
