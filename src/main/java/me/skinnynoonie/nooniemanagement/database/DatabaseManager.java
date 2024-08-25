package me.skinnynoonie.nooniemanagement.database;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.config.DatabaseConfig;
import me.skinnynoonie.nooniemanagement.database.connection.ConnectionException;
import me.skinnynoonie.nooniemanagement.database.connection.ConnectionProvider;
import me.skinnynoonie.nooniemanagement.database.connection.StandardConnectionProviderFactory;
import me.skinnynoonie.nooniemanagement.database.punishment.AsyncPunishmentService;
import me.skinnynoonie.nooniemanagement.database.punishment.PunishmentService;
import me.skinnynoonie.nooniemanagement.database.punishment.StandardPunishmentServiceFactory;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class DatabaseManager {
    private final NoonieManagement noonieManagement;
    private AsyncPunishmentService asyncPunishmentService;
    private boolean initializedProperly;

    public DatabaseManager(@NotNull NoonieManagement noonieManagement) {
        Preconditions.checkArgument(noonieManagement != null, "noonieManagement");

        this.noonieManagement = noonieManagement;
        this.initializedProperly = false;
    }

    public boolean init() {
        Logger logger = this.noonieManagement.getLogger();
        try {
            DatabaseConfig databaseConfig = this.noonieManagement.getConfigManager().getDatabaseConfig();

            ConnectionProvider connectionProvider;
            try {
                connectionProvider = new StandardConnectionProviderFactory().from(databaseConfig);
                if (connectionProvider == null) {
                    logger.severe("Unsupported database type provided.");
                    return false;
                }
            } catch (ConnectionException e) {
                logger.severe("Something went wrong while connecting to the database.");
                logger.severe("Assure that the authentication is correct.");
                return false;
            }

            PunishmentService punishmentService = new StandardPunishmentServiceFactory().from(connectionProvider);
            if (punishmentService == null) {
                logger.severe("Unsupported database type provided.");
                return false;
            }

            this.asyncPunishmentService = new AsyncPunishmentService(punishmentService);
            try {
                this.asyncPunishmentService.init();
            } catch (DatabaseException e) {
                logger.severe("Something went wrong while initializing the database service.");
                e.printStackTrace();
                return false;
            }

            this.initializedProperly = true;
            return true;
        } catch (Exception e) {
            logger.severe("Failed to initialize the database.");
            e.printStackTrace();
            return false;
        }
    }

    public void shutdown() {
        if (!this.initializedProperly) {
            return;
        }

        Logger logger = this.noonieManagement.getLogger();

        try {
            this.asyncPunishmentService.shutdown();
        } catch (Exception e) {
            logger.severe("Failed to shutdown punishment service.");
        }
    }

    public @NotNull AsyncPunishmentService getAsyncPunishmentService() {
        return this.asyncPunishmentService;
    }
}
