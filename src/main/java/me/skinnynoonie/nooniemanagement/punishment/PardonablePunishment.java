package me.skinnynoonie.nooniemanagement.punishment;

public interface PardonablePunishment extends Punishment {
    boolean isPardoned();

    default void pardon() {
        this.pardon(null);
    }

    void pardon(String reason);

    String getPardonReason();
}
