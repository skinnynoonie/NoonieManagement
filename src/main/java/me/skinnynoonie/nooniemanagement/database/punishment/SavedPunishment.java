package me.skinnynoonie.nooniemanagement.database.punishment;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import org.jetbrains.annotations.NotNull;

public final class SavedPunishment<T extends Punishment> {
    private final int id;
    private final T punishment;

    public SavedPunishment(int id, @NotNull T punishment) {
        Preconditions.checkArgument(punishment != null, "punishment");

        this.id = id;
        this.punishment = punishment;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull T getPunishment() {
        return this.punishment;
    }
}
