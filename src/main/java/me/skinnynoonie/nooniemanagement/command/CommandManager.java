package me.skinnynoonie.nooniemanagement.command;

import me.skinnynoonie.nooniemanagement.command.commands.BanCMD;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

// Todo: Honestly just remove this class, register everything in the main class for now.

public class CommandManager {

    private final Plugin plugin;
    private final ManagementDatabase managementDatabase;
    private final List<CustomCommand> commandList = new ArrayList<>();

    public CommandManager(Plugin plugin, ManagementDatabase managementDatabase) {
        this.plugin = plugin;
        this.managementDatabase = managementDatabase;
    }

    public void registerAllCommands() {
        commandList.add(new BanCMD(plugin, managementDatabase));

        commandList.forEach(CustomCommand::register);
    }

    public void unregisterAllCommands() {
        commandList.forEach(CustomCommand::unregister);
    }

}
