package me.skinnynoonie.nooniemanagement.punishment;

public interface DurationalPunishment {
    long getDuration();

    default boolean isPermanent() {
        return this.getDuration() < 0;
    }
}
