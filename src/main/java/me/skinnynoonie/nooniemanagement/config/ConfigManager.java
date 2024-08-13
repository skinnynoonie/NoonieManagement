package me.skinnynoonie.nooniemanagement.config;

import me.skinnynoonie.nooniemanagement.NoonieManagement;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

public final class ConfigManager {
    private final NoonieManagement noonieManagement;
    private VersionConfig versionConfig;

    public ConfigManager(NoonieManagement noonieManagement) {
        this.noonieManagement = noonieManagement;
    }

    public boolean init() {
        this.noonieManagement.saveDefaultConfig();
        ConfigurationSection config = this.noonieManagement.getConfig();
        Logger logger = this.noonieManagement.getLogger();

        this.versionConfig = new StandardVersionConfig(config);
        if (this.versionConfig.isNotValid()) {
            logger.severe("The version configuration is not valid.");
            logger.severe("The section \"version\" in the configuration may have been accidentally deleted.");
            return false;
        } else if (this.versionConfig.isOutdated()) {
            logger.severe("Configuration is outdated!");
            logger.severe("Configuration version is " + this.versionConfig.getVersion() + " but should be " + VersionConfig.VERSION);
            return false;
        }

        return true;
    }

    public VersionConfig getVersionConfig() {
        return this.versionConfig;
    }
}
