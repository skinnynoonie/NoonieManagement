package me.skinnynoonie.nooniemanagement.database.punishment.service;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import me.skinnynoonie.nooniemanagement.database.punishment.repository.postgresql.PostgreSqlPlayerMutePunishmentRepository;
import me.skinnynoonie.nooniemanagement.database.source.DatabaseSourceOptions;
import me.skinnynoonie.nooniemanagement.database.source.PostgreSqlDatabaseSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardPunishmentServiceFactory implements PunishmentServiceFactory {
    @Override
    public @Nullable PunishmentService from(@NotNull DatabaseConfig databaseConfig) {
        Preconditions.checkArgument(databaseConfig != null, "databaseConfig");
        Preconditions.checkArgument(databaseConfig.isValid(), "databaseConfig not valid");

        return switch (databaseConfig.getDatabaseType().toUpperCase()) {
            case "POSTGRESQL" -> this.createPostgreSqlPunishmentService(databaseConfig);
            default -> null;
        };
    }

    private PunishmentService createPostgreSqlPunishmentService(DatabaseConfig databaseConfig) {
        DatabaseSourceOptions options = DatabaseSourceOptions.fromConfig(databaseConfig);
        PostgreSqlDatabaseSource databaseSource = new PostgreSqlDatabaseSource(options);
        return new CombinedPunishmentService(
                new PostgreSqlPlayerMutePunishmentRepository(databaseSource)
        );
    }
}
