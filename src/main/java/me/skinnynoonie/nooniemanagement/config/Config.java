package me.skinnynoonie.nooniemanagement.config;

public interface Config {
    boolean isValid();

    default boolean isNotValid() {
        return !this.isValid();
    }
}
