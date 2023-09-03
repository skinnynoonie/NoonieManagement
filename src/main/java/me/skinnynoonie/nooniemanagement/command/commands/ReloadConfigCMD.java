package me.skinnynoonie.nooniemanagement.command.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;
import me.skinnynoonie.nooniemanagement.command.CustomCommand;
import me.skinnynoonie.nooniemanagement.config.ConfigurableMessageManager;
import me.skinnynoonie.nooniemanagement.permission.EnumPermissionManager;
import org.bukkit.command.CommandSender;

public record ReloadConfigCMD(ConfigurableMessageManager configurableMessageManager,
                              EnumPermissionManager permissionManager)
        implements CustomCommand, ResultingCommandExecutor {

    @Override
    public void register() {
        unregister();
        new CommandTree("nooniepunishments")
                .then(new LiteralArgument("reload")
                        .then(new LiteralArgument("messages")
                                .executes(this))
                        .then(new LiteralArgument("permissions")
                                .executes(this))
                        .then(new MultiLiteralArgument("all", "all", "everything")
                                .executes(this))
                ).register();
    }

    @Override
    public void unregister() {
        CommandAPI.unregister("nooniepunishments");
    }

    @Override
    public int run(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        return 1;
    }
}
