package me.skinnynoonie.nooniemanagement;

import fr.mrmicky.fastinv.FastInvManager;
import me.skinnynoonie.nooniemanagement.command.commands.*;
import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.ConfigurableMessageManager;
import me.skinnynoonie.nooniemanagement.config.organizers.LocalConfigurableMessageOrganizerImpl;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.database.impl.LocalJsonManagementDatabaseImpl;
import me.skinnynoonie.nooniemanagement.listeners.ChatListener;
import me.skinnynoonie.nooniemanagement.listeners.PlayerConnectionListener;
import me.skinnynoonie.nooniemanagement.permission.EnumPermissionManager;
import me.skinnynoonie.nooniemanagement.permission.impl.LocalPermissionManagerImpl;
import me.skinnynoonie.nooniemanagement.permission.permissions.CommandPermissions;
import me.skinnynoonie.nooniemanagement.permission.permissions.PunishmentPermissions;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public final class NoonieManagement extends JavaPlugin {

    private ManagementDatabase managementDatabase;

    @Override
    public void onEnable() {
        FastInvManager.register(this);
        runManagementDatabaseOperations();
        runEnumPermissionManagerOperations();
        runConfigurableMessageOperations();
        runCommandOperations();
        runListenerOperations();
    }

    @Override
    public void onDisable() {
    }

    public ManagementDatabase getManagementDatabase() {
        return this.managementDatabase;
    }

    public static NoonieManagement getInstance() {
        return JavaPlugin.getPlugin(NoonieManagement.class);
    }

    private void runManagementDatabaseOperations() {
        managementDatabase = new ManagementDatabase(new LocalJsonManagementDatabaseImpl(this));
        managementDatabase.initiate();
    }

    private void runEnumPermissionManagerOperations() {
        EnumPermissionManager enumPermissionManager = new EnumPermissionManager(new LocalPermissionManagerImpl(this));
        enumPermissionManager.initiate();
        enumPermissionManager.registerEnumPermissions(CommandPermissions.class);
        enumPermissionManager.reloadEnumPermissions(PunishmentPermissions.class);
    }

    private void runConfigurableMessageOperations() {
        ConfigurableMessageManager configurableMessageManager = new ConfigurableMessageManager(new LocalConfigurableMessageOrganizerImpl(this));
        configurableMessageManager.initiate();
        new Reflections("me.skinnynoonie.nooniemanagement.config.messages.chat")
                .getSubTypesOf(ConfigurableMessage.class)
                .forEach(configurableMessageManager::register);
        new Reflections("me.skinnynoonie.nooniemanagement.config.messages.gui")
                .getSubTypesOf(ConfigurableMessage.class)
                .forEach(configurableMessageManager::register);
    }

    private void runCommandOperations() {
        new BanCMD(this, managementDatabase).register();
        new UnbanCMD(this, managementDatabase).register();
        new MuteCMD(this, managementDatabase).register();
        new UnmuteCMD(this, managementDatabase).register();
        new KickCMD(this, managementDatabase).register();
        new WarnCMD(this, managementDatabase).register();
        new ViewPunishmentLogsCMD(this, managementDatabase).register();
    }

    private void runListenerOperations() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerConnectionListener(managementDatabase), this);
        pluginManager.registerEvents(new ChatListener(managementDatabase), this);
    }


}
