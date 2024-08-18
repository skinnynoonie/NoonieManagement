package me.skinnynoonie.nooniemanagement.punishment;

import java.util.UUID;

public interface PardonablePunishment extends Punishment {
    UUID getPardoner();

    boolean isPardoned();

    void pardon(UUID pardoner, String reason);

    String getPardonReason();
}
