package me.skinnynoonie.nooniemanagement.punishment;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PardonablePunishment extends Punishment {
    @Nullable UUID getPardoner();

    boolean isPardoned();

    void pardon(@Nullable UUID pardoner, @Nullable String reason);

    @Nullable String getPardonReason();
}
