package me.skinnynoonie.nooniemanagement;

import me.skinnynoonie.nooniemanagement.config.ConfigManager;
import me.skinnynoonie.nooniemanagement.database.DatabaseManager;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class NoonieManagement extends JavaPlugin {
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PunishmentManager punishmentManager;

    @Override
    public void onEnable() {
        try {
            this.configManager = new ConfigManager(this);
            if (!this.configManager.init()) {
                this.shutdownWithReason("Shutting down due to invalid configuration.");
                return;
            }

            this.databaseManager = new DatabaseManager(this);
            if (!this.databaseManager.init()) {
                this.shutdownWithReason("Shutting down due to an invalid database.");
                return;
            }

            this.punishmentManager = new PunishmentManager(this);
        } catch (Throwable e) {
            this.shutdownWithReason("Shutting down due to an exception occurring.");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        super.getLogger().info("Shutting down...");
        try {
            this.databaseManager.shutdown();
        } catch (Throwable e) {
            super.getLogger().severe("Something went wrong while shutting down!");
            e.printStackTrace();
        }
    }

    public @NotNull ConfigManager getConfigManager() {
        return this.configManager;
    }

    public @NotNull DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    private void shutdownWithReason(String... reasons) {
        for (String reason : reasons) {
            super.getLogger().severe(reason);
        }
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
