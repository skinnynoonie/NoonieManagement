package me.skinnynoonie.nooniemanagement.database.punishment.factory;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.database.connection.ConnectionProvider;
import me.skinnynoonie.nooniemanagement.database.connection.PostgreSqlConnectionProvider;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import me.skinnynoonie.nooniemanagement.database.punishment.postgresql.PostgreSqlPunishmentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardPunishmentServiceFactory implements PunishmentServiceFactory {
    @Override
    public @Nullable PunishmentService from(@NotNull ConnectionProvider connectionProvider) {
        Preconditions.checkArgument(connectionProvider != null, "connectionProvider");

        if (connectionProvider instanceof PostgreSqlConnectionProvider postgreSqlConnectionProvider) {
            return new PostgreSqlPunishmentService(postgreSqlConnectionProvider);
        } else {
            return null;
        }
    }
}
