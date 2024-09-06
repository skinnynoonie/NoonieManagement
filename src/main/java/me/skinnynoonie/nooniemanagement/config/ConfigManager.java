package me.skinnynoonie.nooniemanagement.config;

import com.google.common.base.Preconditions;
import me.skinnynoonie.nooniemanagement.NoonieManagement;
import me.skinnynoonie.nooniemanagement.config.database.DatabaseConfig;
import me.skinnynoonie.nooniemanagement.config.database.StandardDatabaseConfig;
import me.skinnynoonie.nooniemanagement.config.durationformat.DurationFormatConfig;
import me.skinnynoonie.nooniemanagement.config.durationformat.StandardDurationFormatConfig;
import me.skinnynoonie.nooniemanagement.config.message.MessageConfig;
import me.skinnynoonie.nooniemanagement.config.message.StandardMessageConfig;
import me.skinnynoonie.nooniemanagement.config.permission.PermissionConfig;
import me.skinnynoonie.nooniemanagement.config.permission.StandardPermissionConfig;
import me.skinnynoonie.nooniemanagement.config.version.StandardVersionConfig;
import me.skinnynoonie.nooniemanagement.config.version.VersionConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public final class ConfigManager {
    private final NoonieManagement noonieManagement;
    private final Map<Class<?>, Config> configMap;
    private boolean legalState;

    public ConfigManager(@NotNull NoonieManagement noonieManagement) {
        Preconditions.checkArgument(noonieManagement != null, "noonieManagement");

        this.noonieManagement = noonieManagement;
        this.configMap = new ConcurrentHashMap<>();
        this.legalState = false;
    }

    private interface StandardConfigFactory<C extends Config> {
        C from(ConfigurationSection config);
    }

    public boolean init() {
        Logger logger = this.noonieManagement.getLogger();
        try {
            this.noonieManagement.saveDefaultConfig();
            ConfigurationSection config = YamlConfiguration.loadConfiguration(
                    this.noonieManagement.getDataPath().resolve("config.yml").toFile()
            );

            if (this.loadConfig(config, "version", VersionConfig.class, StandardVersionConfig::from)) {
                VersionConfig versionConfig = (VersionConfig) this.configMap.get(VersionConfig.class);
                if (versionConfig.isOutdated()) {
                    logger.severe("[ConfigManager] The latest version is " + VersionConfig.VERSION + " but your version is " + versionConfig.getVersion());
                    return false;
                }
            } else {
                return false;
            }

            this.legalState = this.loadConfig(config, "database", DatabaseConfig.class, StandardDatabaseConfig::from) &&
                    this.loadConfig(config, "permission", PermissionConfig.class, StandardPermissionConfig::from) &&
                    this.loadConfig(config, "message", MessageConfig.class, StandardMessageConfig::from) &&
                    this.loadConfig(config, "duration format", DurationFormatConfig.class, StandardDurationFormatConfig::from);

            return this.legalState;
        } catch (Exception e) {
            logger.severe("[ConfigManager] Failed to initialize because an unexpected exception occurred.");
            e.printStackTrace();
            return false;
        }
    }

    private boolean loadConfig(ConfigurationSection config, String configName, Class<?> configClass, StandardConfigFactory<?> configFactory) {
        Logger logger = this.noonieManagement.getLogger();
        try {
            this.configMap.put(configClass, configFactory.from(config));
            logger.info("[ConfigManager] Successfully loaded the " + configName + " configuration.");
            return true;
        } catch (Exception e) {
            logger.severe("[ConfigManager] Failed to load the " + configName + " configuration.");
            e.printStackTrace();
            return false;
        }
    }

    public @NotNull VersionConfig getVersionConfig() {
        this.throwIfInvalidState();

        return (VersionConfig) this.configMap.get(VersionConfig.class);
    }

    public @NotNull DatabaseConfig getDatabaseConfig() {
        this.throwIfInvalidState();

        return (DatabaseConfig) this.configMap.get(DatabaseConfig.class);
    }

    public @NotNull MessageConfig getMessageConfig() {
        this.throwIfInvalidState();

        return (MessageConfig) this.configMap.get(MessageConfig.class);
    }

    public @NotNull PermissionConfig getPermissionConfig() {
        this.throwIfInvalidState();

        return (PermissionConfig) this.configMap.get(PermissionConfig.class);
    }

    public @NotNull DurationFormatConfig getDurationFormatConfig() {
        this.throwIfInvalidState();

        return (DurationFormatConfig) this.configMap.get(DurationFormatConfig.class);
    }

    private void throwIfInvalidState() {
        if (!this.legalState) {
            throw new IllegalStateException("config manager is in an illegal state");
        }
    }
}
