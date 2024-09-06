package me.skinnynoonie.nooniemanagement;

import me.skinnynoonie.nooniemanagement.config.ConfigManager;
import me.skinnynoonie.nooniemanagement.database.DatabaseManager;
import me.skinnynoonie.nooniemanagement.duration.DurationParser;
import me.skinnynoonie.nooniemanagement.duration.StandardDurationParser;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class NoonieManagement extends JavaPlugin {
    private static NoonieManagement instance;
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PunishmentManager punishmentManager;
    private DurationParser durationParser;

    @Override
    public void onEnable() {
        instance = this;
        Logger logger = super.getLogger();

        logger.info("===========================================================");
        logger.info("Enabling...");
        logger.info("");

        try {
            this.configManager = new ConfigManager(this);
            if (this.configManager.init()) {
                logger.info("[ConfigManager] Successfully initialized.");
            } else {
                logger.severe("[ConfigManager] Failed to initialize.");
                this.shutdownWithReason("Shutting down due to failed config manager initialization.");
                return;
            }

            this.durationParser = new StandardDurationParser(this.configManager.getDurationFormatConfig());

            this.databaseManager = new DatabaseManager(this);
            if (this.databaseManager.init()) {
                logger.info("[DatabaseManager] Successfully initialized.");
            } else {
                logger.severe("[DatabaseManager] Failed to initialize.");
                this.shutdownWithReason("Shutting down due to failed database manager initialization.");
                return;
            }

            this.punishmentManager = new PunishmentManager(this);
        } catch (Throwable e) {
            this.shutdownWithReason("Shutting down due to an unexpected exception occurring.");
            e.printStackTrace();
        } finally {
            logger.info("");
            logger.info("===========================================================");
        }
    }

    @Override
    public void onDisable() {
        Logger logger = super.getLogger();

        logger.info("===========================================================");
        logger.info("Disabling...");
        logger.info("");

        try {
            if (this.databaseManager != null) {
                if (this.databaseManager.shutdown()) {
                    logger.info("[DatabaseManager] Successfully shutdown.");
                } else {
                    logger.severe("[DatabaseManager] Failed to shutdown properly.");
                }
            }
        } catch (Throwable e) {
            logger.severe("Failed to shutdown properly as an unexpected exception occurred.");
            e.printStackTrace();
        }

        logger.info("");
        logger.info("===========================================================");
    }

    public static @NotNull NoonieManagement get() {
        return instance;
    }

    public @NotNull ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NotNull DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public @NotNull PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }

    public @NotNull DurationParser getDurationParser() {
        return this.durationParser;
    }

    private void shutdownWithReason(String... reasons) {
        for (String reason : reasons) {
            super.getLogger().severe(reason);
        }
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
