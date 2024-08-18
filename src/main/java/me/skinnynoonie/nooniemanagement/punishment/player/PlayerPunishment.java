package me.skinnynoonie.nooniemanagement.punishment.player;

import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerPunishment extends Punishment {
    @NotNull UUID getTarget();
}
