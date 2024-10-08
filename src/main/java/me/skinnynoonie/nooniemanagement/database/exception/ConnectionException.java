package me.skinnynoonie.nooniemanagement.database.exception;

import org.jetbrains.annotations.Nullable;

public final class ConnectionException extends DatabaseException {
    public ConnectionException() {
    }

    public ConnectionException(@Nullable String message) {
        super(message);
    }

    public ConnectionException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public ConnectionException(@Nullable Throwable cause) {
        super(cause);
    }
}
