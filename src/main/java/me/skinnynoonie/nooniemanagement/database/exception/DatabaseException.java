package me.skinnynoonie.nooniemanagement.database.exception;

import org.jetbrains.annotations.Nullable;

public class DatabaseException extends RuntimeException {
    public DatabaseException() {
    }

    public DatabaseException(@Nullable String message) {
        super(message);
    }

    public DatabaseException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(@Nullable Throwable cause) {
        super(cause);
    }
}
