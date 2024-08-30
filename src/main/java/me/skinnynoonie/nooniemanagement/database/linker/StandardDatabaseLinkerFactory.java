package me.skinnynoonie.nooniemanagement.database.linker;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardDatabaseLinkerFactory implements DatabaseLinkerFactory {
    @Override
    public @Nullable DatabaseLinker from(@NotNull DatabaseConfig databaseConfig) {
        Preconditions.checkArgument(databaseConfig != null, "databaseConfig");
        Preconditions.checkArgument(databaseConfig.isValid(), "databaseConfig not valid");

        SqlConnectionOptions options = SqlConnectionOptions.fromConfig(databaseConfig);
        return switch (databaseConfig.getDatabaseType().toUpperCase()) {
            case "POSTGRESQL" -> new PostgreSqlDatabaseLinker(options);
            default -> null;
        };
    }
}
