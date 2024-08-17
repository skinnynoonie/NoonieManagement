package me.skinnynoonie.nooniemanagement.database.punishment;

import me.skinnynoonie.nooniemanagement.punishment.Punishment;

public final class SavedPunishment<T extends Punishment> {
    private final int id;
    private final T punishment;

    public SavedPunishment(int id, T punishment) {
        this.id = id;
        this.punishment = punishment;
    }

    public int getId() {
        return this.id;
    }

    public T getPunishment() {
        return this.punishment;
    }
}
