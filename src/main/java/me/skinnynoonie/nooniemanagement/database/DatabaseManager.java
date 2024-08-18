package me.skinnynoonie.nooniemanagement.database;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.config.ConfigManager;
import me.skinnynoonie.nooniemanagement.database.punishment.service.PunishmentService;
import me.skinnynoonie.nooniemanagement.database.punishment.service.PunishmentServiceFactory;
import me.skinnynoonie.nooniemanagement.database.punishment.service.StandardPunishmentServiceFactory;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class DatabaseManager {
    private final NoonieManagement noonieManagement;
    private final PunishmentServiceFactory punishmentServiceFactory;
    private PunishmentService punishmentService;
    private boolean initializedProperly;

    public DatabaseManager(@NotNull NoonieManagement noonieManagement) {
        Preconditions.checkArgument(noonieManagement != null, "noonieManagement");

        this.noonieManagement = noonieManagement;
        this.punishmentServiceFactory = new StandardPunishmentServiceFactory();
        this.initializedProperly = false;
    }

    public boolean init() {
        Logger logger = this.noonieManagement.getLogger();

        if (!this.createPunishmentService(logger)) {
            return false;
        }

        try {
            this.punishmentService.init();
        } catch (Exception e) {
            logger.severe("Failed database initialization.");
            e.printStackTrace();
            return false;
        }

        this.initializedProperly = true;
        return true;
    }

    private boolean createPunishmentService(Logger logger) {
        ConfigManager configManager = this.noonieManagement.getConfigManager();
        try {
            this.punishmentService = this.punishmentServiceFactory.from(configManager.getDatabaseConfig());
        } catch (Exception e) {
            logger.severe(
                    """
                    Failed database connection.
                    Please assure the host, port, username, password, etc. are all correct.
                    """
            );
            e.printStackTrace();
            return false;
        }

        if (this.punishmentService == null) {
            logger.severe("Unsupported database type provided.");
            return false;
        }

        return true;
    }

    public void shutdown() {
        if (!this.initializedProperly) {
            return;
        }

        Logger logger = this.noonieManagement.getLogger();

        try {
            this.punishmentService.shutdown();
        } catch (Exception e) {
            logger.severe("Failed to shutdown punishment service.");
        }
    }

    public @NotNull PunishmentService getPunishmentService() {
        return this.punishmentService;
    }
}
