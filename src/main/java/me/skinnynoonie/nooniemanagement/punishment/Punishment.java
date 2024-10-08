package me.skinnynoonie.nooniemanagement.punishment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Punishment extends Cloneable {
    @NotNull String getType();

    @Nullable UUID getIssuer();

    @Nullable String getReason();

    long getTimeOccurred();

    @NotNull Punishment clone();
}
