package me.skinnynoonie.nooniemanagement.database;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class Saved<T> {
    private final int id;
    private final T saved;

    public Saved(int id, @NotNull T saved) {
        Preconditions.checkArgument(saved != null, "saved");

        this.id = id;
        this.saved = saved;
    }

    public int getId() {
        return this.id;
    }

    public @NotNull T get() {
        return this.saved;
    }
}
