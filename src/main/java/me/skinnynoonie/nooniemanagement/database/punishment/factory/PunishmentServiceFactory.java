package me.skinnynoonie.nooniemanagement.database.punishment.factory;

import me.skinnynoonie.nooniemanagement.database.linker.DatabaseLinker;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PunishmentServiceFactory {
    @Nullable PunishmentService from(@NotNull DatabaseLinker databaseLinker);
}
