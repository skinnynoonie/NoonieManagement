package me.skinnynoonie.nooniemanagement.config;

import me.skinnynoonie.nooniemanagement.NoonieManagement;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

public final class ConfigManager {
    private final NoonieManagement noonieManagement;
    private VersionConfig versionConfig;
    private DatabaseConfig databaseConfig;

    public ConfigManager(NoonieManagement noonieManagement) {
        this.noonieManagement = noonieManagement;
    }

    public boolean init() {
        this.noonieManagement.saveDefaultConfig();
        ConfigurationSection config = this.noonieManagement.getConfig();
        Logger logger = this.noonieManagement.getLogger();

        if (!this.initVersionConfig(config, logger)) {
            return false;
        }

        if (!this.initDatabaseConfig(config, logger)) {
            return false;
        }

        return true;
    }

    private boolean initVersionConfig(ConfigurationSection config, Logger logger) {
        this.versionConfig = new StandardVersionConfig(config);
        if (this.versionConfig.isNotValid()) {
            logger.severe("The version configuration is not valid.");
            logger.severe("The section \"version\" in the configuration may have been accidentally deleted.");
            return false;
        } else if (this.versionConfig.isOutdated()) {
            logger.severe("Configuration is outdated!");
            logger.severe("Configuration version is " + this.versionConfig.getVersion() + " but should be " + VersionConfig.VERSION);
            return false;
        } else {
            logger.info("Successfully loaded the version config.");
            return true;
        }
    }

    private boolean initDatabaseConfig(ConfigurationSection config, Logger logger) {
        this.databaseConfig = new StandardDatabaseConfig(config);
        if (this.databaseConfig.isNotValid()) {
            logger.severe("The database config is not valid.");
            logger.severe("The section database.type may not have a valid value.");
            return false;
        } else {
            logger.info("Successfully loaded the database config.");
            return true;
        }
    }

    public VersionConfig getVersionConfig() {
        return this.versionConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return this.databaseConfig;
    }
}
