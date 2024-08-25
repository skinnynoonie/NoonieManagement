package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class ConfigManager {
    private final NoonieManagement noonieManagement;
    private VersionConfig versionConfig;
    private DatabaseConfig databaseConfig;
    private MessageConfig messageConfig;

    public ConfigManager(@NotNull NoonieManagement noonieManagement) {
        Preconditions.checkArgument(noonieManagement != null, "noonieManagement");

        this.noonieManagement = noonieManagement;
    }

    public boolean init() {
        Logger logger = this.noonieManagement.getLogger();
        try {
            this.noonieManagement.saveDefaultConfig();
            ConfigurationSection config = this.noonieManagement.getConfig();

            if (!this.initVersionConfig(config, logger)) {
                return false;
            }

            this.databaseConfig = new StandardDatabaseConfig(config);
            this.messageConfig = new StandardMessageConfig(config);

            return true;
        } catch (Exception e) {
            logger.severe("Failed to initialize configuration.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean initVersionConfig(ConfigurationSection config, Logger logger) {
        this.versionConfig = new StandardVersionConfig(config);
        if (this.versionConfig.isOutdated()) {
            logger.severe("Configuration is outdated!");
            logger.severe("Configuration version is " + this.versionConfig.getVersion() + " but should be " + VersionConfig.VERSION);
            return false;
        } else {
            logger.info("Configuration version is up to date.");
            return true;
        }
    }

    public @NotNull VersionConfig getVersionConfig() {
        return this.versionConfig;
    }

    public @NotNull DatabaseConfig getDatabaseConfig() {
        return this.databaseConfig;
    }

    public MessageConfig getMessageConfig() {
        return this.messageConfig;
    }
}
