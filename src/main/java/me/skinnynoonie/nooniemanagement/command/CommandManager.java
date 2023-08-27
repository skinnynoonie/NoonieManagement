package me.skinnynoonie.nooniemanagement.command;

import me.skinnynoonie.nooniemanagement.command.commands.BanCMD;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    protected final ManagementDatabase managementDatabase;
    private final List<CustomCommand> commandList = new ArrayList<>();

    public CommandManager(ManagementDatabase managementDatabase) {
        this.managementDatabase = managementDatabase;
    }

    public void registerAllCommands() {
        commandList.add(new BanCMD(managementDatabase));

        commandList.forEach(CustomCommand::register);
    }

    public void unregisterAllCommands() {
        commandList.forEach(CustomCommand::unregister);
    }

}
