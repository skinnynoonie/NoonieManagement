package me.skinnynoonie.nooniemanagement.database.punishment.service;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.database.connection.ConnectionProvider;
import me.skinnynoonie.nooniemanagement.database.connection.PostgreSqlConnectionProvider;
import me.skinnynoonie.nooniemanagement.database.punishment.repository.postgresql.PostgreSqlPlayerMutePunishmentRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardPunishmentServiceFactory implements PunishmentServiceFactory {
    @Override
    public @Nullable PunishmentService from(@NotNull ConnectionProvider connectionProvider) {
        Preconditions.checkArgument(connectionProvider != null, "connectionProvider");

        if (connectionProvider instanceof PostgreSqlConnectionProvider postgreSqlConnectionProvider) {
            return this.createPostgreSqlPunishmentService(postgreSqlConnectionProvider);
        } else {
            return null;
        }
    }

    private PunishmentService createPostgreSqlPunishmentService(PostgreSqlConnectionProvider connectionProvider) {
        return new CombinedPunishmentService(
                new PostgreSqlPlayerMutePunishmentRepository(connectionProvider)
        );
    }
}
