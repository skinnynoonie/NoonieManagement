package me.skinnynoonie.nooniemanagement.database.connection;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardConnectionProviderFactory implements ConnectionProviderFactory {
    @Override
    public @Nullable ConnectionProvider from(@NotNull DatabaseConfig databaseConfig) throws ConnectionException {
        Preconditions.checkArgument(databaseConfig != null, "databaseConfig");
        Preconditions.checkArgument(databaseConfig.isValid(), "databaseConfig not valid");

        ConnectionProviderOptions options = ConnectionProviderOptions.fromConfig(databaseConfig);
        return switch (databaseConfig.getDatabaseType().toUpperCase()) {
            case "POSTGRESQL" -> new PostgreSqlConnectionProvider(options);
            default -> null;
        };
    }
}
