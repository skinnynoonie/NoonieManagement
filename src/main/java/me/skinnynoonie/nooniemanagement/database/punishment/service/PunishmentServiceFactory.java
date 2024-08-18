package me.skinnynoonie.nooniemanagement.database.punishment.service;

import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PunishmentServiceFactory {
    @Nullable PunishmentService from(@NotNull DatabaseConfig databaseConfig);
}
