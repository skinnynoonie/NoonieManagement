package me.skinnynoonie.nooniemanagement.database;

import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;

import java.util.UUID;

public interface ManagementDatabaseImpl {

    void initiate() throws Exception;

    void savePunishmentPortfolio(PunishmentPortfolio portfolio) throws Exception;

    PunishmentPortfolio getPunishmentPortfolioByUUID(UUID uuid) throws Exception;
}
