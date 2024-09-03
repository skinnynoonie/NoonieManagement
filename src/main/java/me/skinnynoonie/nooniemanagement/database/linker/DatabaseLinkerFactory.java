package me.skinnynoonie.nooniemanagement.database.linker;

import me.skinnynoonie.nooniemanagement.config.database.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DatabaseLinkerFactory {
    @Nullable DatabaseLinker from(@NotNull DatabaseConfig databaseConfig);
}
