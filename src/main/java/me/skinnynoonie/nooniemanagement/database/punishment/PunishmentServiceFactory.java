package me.skinnynoonie.nooniemanagement.database.punishment;

import me.skinnynoonie.nooniemanagement.database.connection.ConnectionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PunishmentServiceFactory {
    @Nullable PunishmentService from(@NotNull ConnectionProvider connectionProvider);
}
