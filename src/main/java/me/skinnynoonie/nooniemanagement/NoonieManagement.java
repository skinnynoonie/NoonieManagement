package me.skinnynoonie.nooniemanagement;

import me.skinnynoonie.nooniemanagement.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoonieManagement extends JavaPlugin {
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        try {
            this.configManager = new ConfigManager(this);
            if (!this.configManager.init()) {
                this.shutdownWithReason("Shutting down due to invalid configuration.");
                return;
            }
        } catch (Throwable e) {
            this.shutdownWithReason("Shutting down due to an exception occurring.");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    private void shutdownWithReason(String reason) {
        super.getLogger().severe(reason);
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
