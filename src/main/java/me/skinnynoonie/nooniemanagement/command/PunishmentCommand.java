package me.skinnynoonie.nooniemanagement.command;

import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import org.bukkit.plugin.Plugin;

public abstract class PunishmentCommand implements CustomCommand {

    protected final Plugin plugin;
    protected final ManagementDatabase managementDatabase;

    protected PunishmentCommand(Plugin plugin, ManagementDatabase managementDatabase) {
        this.plugin = plugin;
        this.managementDatabase = managementDatabase;
    }

}
