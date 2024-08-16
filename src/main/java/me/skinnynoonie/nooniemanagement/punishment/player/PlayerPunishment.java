package me.skinnynoonie.nooniemanagement.punishment.player;

import me.skinnynoonie.nooniemanagement.punishment.Punishment;

import java.util.UUID;

public interface PlayerPunishment extends Punishment {
    UUID getTarget();
}
