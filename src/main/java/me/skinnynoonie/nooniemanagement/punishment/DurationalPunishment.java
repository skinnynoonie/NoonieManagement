package me.skinnynoonie.nooniemanagement.punishment;

import java.time.Duration;

public interface DurationalPunishment {
    Duration getDuration();

    default boolean isPermanent() {
        return this.getDuration().isNegative();
    }
}
