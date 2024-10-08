package me.skinnynoonie.nooniemanagement.database.linker;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.database.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardDatabaseLinkerFactory implements DatabaseLinkerFactory {
    @Override
    public @Nullable DatabaseLinker from(@NotNull DatabaseConfig databaseConfig) {
        Preconditions.checkArgument(databaseConfig != null, "databaseConfig");

        SqlConnectionOptions options = SqlConnectionOptions.fromConfig(databaseConfig);
        return switch (databaseConfig.getDatabaseType().toUpperCase()) {
            case "POSTGRESQL" -> new PostgreSqlDatabaseLinker(options);
            default -> null;
        };
    }
}
