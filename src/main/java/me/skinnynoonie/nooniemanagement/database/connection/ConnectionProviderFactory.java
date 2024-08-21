package me.skinnynoonie.nooniemanagement.database.connection;

import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConnectionProviderFactory {
    @Nullable ConnectionProvider from(@NotNull DatabaseConfig databaseConfig) throws ConnectionException;
}
