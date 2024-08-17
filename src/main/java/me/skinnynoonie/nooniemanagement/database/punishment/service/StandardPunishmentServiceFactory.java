package me.skinnynoonie.nooniemanagement.database.punishment.service;

import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import me.skinnynoonie.nooniemanagement.database.punishment.repository.postgresql.PostgreSqlPlayerMutePunishmentRepository;
import me.skinnynoonie.nooniemanagement.database.source.DatabaseSourceOptions;
import me.skinnynoonie.nooniemanagement.database.source.PostgreSqlDatabaseSource;

public final class StandardPunishmentServiceFactory implements PunishmentServiceFactory {
    @Override
    public PunishmentService from(DatabaseConfig databaseConfig) {
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
