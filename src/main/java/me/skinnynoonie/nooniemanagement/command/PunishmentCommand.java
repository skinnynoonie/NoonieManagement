package me.skinnynoonie.nooniemanagement.command;

import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import org.bukkit.plugin.Plugin;

// todo: Delete this class before it's too late, I feel like this class only serves the purpose of my auto completing in my ide.
public abstract class PunishmentCommand implements CustomCommand {

    protected final Plugin plugin;
    protected final ManagementDatabase managementDatabase;

    protected PunishmentCommand(Plugin plugin, ManagementDatabase managementDatabase) {
        this.plugin = plugin;
        this.managementDatabase = managementDatabase;
    }

}
