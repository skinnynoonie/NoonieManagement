package me.skinnynoonie.nooniemanagement;

import dev.jorel.commandapi.CommandAPI;
import me.skinnynoonie.nooniemanagement.command.CommandManager;
import me.skinnynoonie.nooniemanagement.config.ConfigurableMessageManager;
import me.skinnynoonie.nooniemanagement.config.messages.PublicBanAnnouncement;
import me.skinnynoonie.nooniemanagement.config.organizers.LocalConfigurableMessageOrganizerImpl;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.database.impl.LocalJsonManagementDatabaseImpl;
import me.skinnynoonie.nooniemanagement.permission.EnumPermissionManagerImpl;
import me.skinnynoonie.nooniemanagement.permission.impl.LocalPermissionManagerImpl;
import me.skinnynoonie.nooniemanagement.permission.permissions.NooniePermissions;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoonieManagement extends JavaPlugin {

    private ManagementDatabase managementDatabase;
    private EnumPermissionManagerImpl permissionManager;
    private ConfigurableMessageManager configurableMessageManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        managementDatabase = new ManagementDatabase(new LocalJsonManagementDatabaseImpl(this));
        managementDatabase.initiate();

        configurableMessageManager = new ConfigurableMessageManager(new LocalConfigurableMessageOrganizerImpl(this));
        configurableMessageManager.initiate();
        configurableMessageManager.registerAllMessages();

        permissionManager = new LocalPermissionManagerImpl(this);
        permissionManager.registerPermissions(NooniePermissions.class);
        System.out.println(NooniePermissions.VIEW_SILENT_PUNISHMENTS.getPermission());

        commandManager = new CommandManager(this, managementDatabase);
        commandManager.registerAllCommands();


        System.out.println(new PublicBanAnnouncement(NameableUser.UNKNOWN, NameableUser.UNKNOWN).getFormatted());

    }

    @Override
    public void onDisable() {
        CommandAPI.unregister("ban");
    }

    public ManagementDatabase getManagementDatabase() {
        return this.managementDatabase;
    }

    public static NoonieManagement getInstance() {
        return JavaPlugin.getPlugin(NoonieManagement.class);
    }

}
