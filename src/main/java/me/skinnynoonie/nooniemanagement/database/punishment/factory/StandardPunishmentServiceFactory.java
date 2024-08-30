package me.skinnynoonie.nooniemanagement.database.punishment.factory;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.database.linker.DatabaseLinker;
import me.skinnynoonie.nooniemanagement.database.linker.PostgreSqlDatabaseLinker;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import me.skinnynoonie.nooniemanagement.database.punishment.postgresql.PostgreSqlPunishmentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StandardPunishmentServiceFactory implements PunishmentServiceFactory {
    @Override
    public @Nullable PunishmentService from(@NotNull DatabaseLinker databaseLinker) {
        Preconditions.checkArgument(databaseLinker != null, "databaseLinker");

        if (databaseLinker instanceof PostgreSqlDatabaseLinker postgreSqlDatabaseLinker) {
            return new PostgreSqlPunishmentService(postgreSqlDatabaseLinker);
        } else {
            return null;
        }
    }
}
