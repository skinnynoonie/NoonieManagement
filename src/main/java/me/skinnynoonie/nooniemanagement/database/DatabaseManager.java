package me.skinnynoonie.nooniemanagement.database;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.config.database.DatabaseConfig;
import me.skinnynoonie.nooniemanagement.database.exception.ConnectionException;
import me.skinnynoonie.nooniemanagement.database.exception.DatabaseException;
import me.skinnynoonie.nooniemanagement.database.linker.DatabaseLinker;
import me.skinnynoonie.nooniemanagement.database.linker.StandardDatabaseLinkerFactory;
import me.skinnynoonie.nooniemanagement.database.punishment.AsyncPunishmentService;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import me.skinnynoonie.nooniemanagement.database.punishment.factory.StandardPunishmentServiceFactory;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class DatabaseManager {
    private final NoonieManagement noonieManagement;
    private AsyncPunishmentService asyncPunishmentService;
    private boolean legalState;

    public DatabaseManager(@NotNull NoonieManagement noonieManagement) {
        Preconditions.checkArgument(noonieManagement != null, "noonieManagement");

        this.noonieManagement = noonieManagement;
        this.legalState = false;
    }

    public boolean init() {
        Logger logger = this.noonieManagement.getLogger();
        try {
            DatabaseConfig databaseConfig = this.noonieManagement.getConfigManager().getDatabaseConfig();
            DatabaseLinker databaseLinker = new StandardDatabaseLinkerFactory().from(databaseConfig);
            if (databaseLinker == null) {
                logger.severe("[DatabaseManager] Unsupported database type provided.");
                return false;
            }

            PunishmentService punishmentService = new StandardPunishmentServiceFactory().from(databaseLinker);
            if (punishmentService == null) {
                logger.severe("[DatabaseManager] Unsupported database type provided.");
                return false;
            }

            logger.info("[DatabaseManager] Successfully created the punishment service.");

            this.asyncPunishmentService = new AsyncPunishmentService(punishmentService);
            try {
                this.asyncPunishmentService.init();
            } catch (ConnectionException e) {
                logger.severe("[DatabaseManager] Something went wrong while connecting to the database.");
                logger.severe("[DatabaseManager] Assure that the connection authentication are correct.");
                e.printStackTrace();
                return false;
            } catch (DatabaseException e) {
                logger.severe("[DatabaseManager] Something went wrong while initializing the punishment service.");
                e.printStackTrace();
                return false;
            }

            logger.info("[DatabaseManager] Successfully initialized the punishment service.");

            this.legalState = true;
            return true;
        } catch (Exception e) {
            logger.severe("[DatabaseManager] Failed to initialize because an unexpected exception occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean shutdown() {
        if (!this.legalState) {
            return true;
        }

        Logger logger = this.noonieManagement.getLogger();

        try {
            this.asyncPunishmentService.shutdown();
            return true;
        } catch (Exception e) {
            logger.severe("[DatabaseManager] Failed to shutdown because an unexpected exception occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public @NotNull AsyncPunishmentService getAsyncPunishmentService() {
        this.throwIfInvalidState();
        return this.asyncPunishmentService;
    }

    private void throwIfInvalidState() {
        if (!this.legalState) {
            throw new IllegalStateException("database manager is in an illegal state");
        }
    }
}
