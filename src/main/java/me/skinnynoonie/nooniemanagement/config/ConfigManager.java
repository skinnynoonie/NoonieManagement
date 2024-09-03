package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.config.database.DatabaseConfig;
import me.skinnynoonie.nooniemanagement.config.database.StandardDatabaseConfig;
import me.skinnynoonie.nooniemanagement.config.message.MessageConfig;
import me.skinnynoonie.nooniemanagement.config.message.StandardMessageConfig;
import me.skinnynoonie.nooniemanagement.config.permission.PermissionConfig;
import me.skinnynoonie.nooniemanagement.config.permission.StandardPermissionConfig;
import me.skinnynoonie.nooniemanagement.config.version.StandardVersionConfig;
import me.skinnynoonie.nooniemanagement.config.version.VersionConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class ConfigManager {
    private final NoonieManagement noonieManagement;
    private boolean legalState;
    private VersionConfig versionConfig;
    private DatabaseConfig databaseConfig;
    private MessageConfig messageConfig;
    private PermissionConfig permissionConfig;

    public ConfigManager(@NotNull NoonieManagement noonieManagement) {
        Preconditions.checkArgument(noonieManagement != null, "noonieManagement");

        this.noonieManagement = noonieManagement;
    }

    public boolean init() {
        Logger logger = this.noonieManagement.getLogger();
        try {
            this.noonieManagement.saveDefaultConfig();
            ConfigurationSection config = this.noonieManagement.getConfig();

            this.legalState = this.loadVersionConfig(config, logger) &&
                    this.loadDatabaseConfig(config, logger) &&
                    this.loadMessageConfig(config, logger) &&
                    this.loadPermissionConfig(config, logger);
            return this.legalState;
        } catch (Exception e) {
            logger.severe("[ConfigManager] Failed to initialize because an unexpected exception occurred.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean loadVersionConfig(ConfigurationSection config, Logger logger) {
        this.versionConfig = new StandardVersionConfig(config);
        if (this.versionConfig.isNotValid()) {
            logger.severe("[ConfigManager] Invalid configuration version.");
            return false;
        } else if (this.versionConfig.isOutdated()) {
            logger.severe("[ConfigManager] Configuration is outdated.");
            logger.severe("[ConfigManager] Configuration version is " + this.versionConfig.getVersion() + " but should be " + VersionConfig.VERSION);
            return false;
        } else {
            logger.info("[ConfigManager] Configuration version is up to date.");
            return true;
        }
    }

    private boolean loadDatabaseConfig(ConfigurationSection config, Logger logger) {
        this.databaseConfig = new StandardDatabaseConfig(config);
        if (this.databaseConfig.isNotValid()) {
            logger.severe("[ConfigManager] Invalid database configuration.");
            return false;
        } else {
            logger.info("[ConfigManager] Loaded the database configuration.");
            return true;
        }
    }

    private boolean loadMessageConfig(ConfigurationSection config, Logger logger) {
        this.messageConfig = new StandardMessageConfig(config);
        if (this.messageConfig.isNotValid()) {
            logger.severe("[ConfigManager] Invalid message configuration.");
            return false;
        } else {
            logger.info("[ConfigManager] Loaded the message configuration.");
            return true;
        }
    }

    private boolean loadPermissionConfig(ConfigurationSection config, Logger logger) {
        this.permissionConfig = new StandardPermissionConfig(config);
        if (this.permissionConfig.isNotValid()) {
            logger.severe("[ConfigManager] Invalid permission configuration.");
            return false;
        } else {
            logger.info("[ConfigManager] Loaded the permission configuration.");
            return true;
        }
    }

    public @NotNull VersionConfig getVersionConfig() {
        this.throwIfInvalidState();
        return this.versionConfig;
    }

    public @NotNull DatabaseConfig getDatabaseConfig() {
        this.throwIfInvalidState();
        return this.databaseConfig;
    }

    public @NotNull MessageConfig getMessageConfig() {
        this.throwIfInvalidState();
        return this.messageConfig;
    }

    public @NotNull PermissionConfig getPermissionConfig() {
        this.throwIfInvalidState();
        return this.permissionConfig;
    }

    private void throwIfInvalidState() {
        if (!this.legalState) {
            throw new IllegalStateException("config manager is in an illegal state");
        }
    }
}
