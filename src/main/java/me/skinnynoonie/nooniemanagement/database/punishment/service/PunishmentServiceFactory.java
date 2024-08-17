package me.skinnynoonie.nooniemanagement.database.punishment.service;

import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;

public interface PunishmentServiceFactory {
    PunishmentService from(DatabaseConfig databaseConfig);
}
