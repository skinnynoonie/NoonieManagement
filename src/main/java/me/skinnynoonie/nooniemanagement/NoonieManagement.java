package me.skinnynoonie.nooniemanagement;

import dev.jorel.commandapi.CommandAPI;
import me.skinnynoonie.nooniemanagement.command.CommandManager;
import me.skinnynoonie.nooniemanagement.config.ConfigurableMessageManager;
import me.skinnynoonie.nooniemanagement.config.messages.PublicBanAnnouncement;
import me.skinnynoonie.nooniemanagement.config.organizers.LocalConfigurableMessageOrganizerImpl;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.database.impl.LocalJsonManagementDatabaseImpl;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoonieManagement extends JavaPlugin {

    private ManagementDatabase managementDatabase;
    private CommandManager commandManager;
    private ConfigurableMessageManager configurableMessageManager;

    @Override
    public void onEnable() {
        managementDatabase = new ManagementDatabase(new LocalJsonManagementDatabaseImpl(this));
        managementDatabase.initiate();

        commandManager = new CommandManager(managementDatabase);
        commandManager.registerAllCommands();

        configurableMessageManager = new ConfigurableMessageManager(new LocalConfigurableMessageOrganizerImpl(this));
        configurableMessageManager.initiate();
        configurableMessageManager.registerAllMessages();

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
