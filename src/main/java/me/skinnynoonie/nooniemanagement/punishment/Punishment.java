package me.skinnynoonie.nooniemanagement.punishment;

import java.util.UUID;

public interface Punishment {
    String getType();

    UUID getIssuer();

    String getReason();

    long getTimeOccurred();
}
