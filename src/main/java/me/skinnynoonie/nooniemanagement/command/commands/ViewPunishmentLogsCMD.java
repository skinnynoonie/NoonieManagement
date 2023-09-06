package me.skinnynoonie.nooniemanagement.command.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import me.skinnynoonie.nooniemanagement.command.CustomCommand;
import me.skinnynoonie.nooniemanagement.command.arguments.NameableUserArgument;
import me.skinnynoonie.nooniemanagement.config.messages.chat.InternalErrorMessage;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.guis.PunishmentLogsGUI;
import me.skinnynoonie.nooniemanagement.permission.permissions.CommandPermissions;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.ExecutionException;

public record ViewPunishmentLogsCMD(Plugin plugin, ManagementDatabase managementDatabase) implements CustomCommand, PlayerCommandExecutor {

    @Override
    public void register() {
        unregister();
        new CommandAPICommand("viewpunishmentlogs")
                .withPermission(CommandPermissions.VIEW_PUNISHMENT_LOGS_COMMAND.getPermission())
                .withArguments(NameableUserArgument.get("target"))
                .executesPlayer(this)
                .register();
    }

    @Override
    public void unregister() {
        CommandAPI.unregister("viewpunishmentlogs");
    }

    @Override
    public void run(Player sender, CommandArguments args) throws WrapperCommandSyntaxException {
        NameableUser target = (NameableUser) args.get("target");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                target.loadAndCache();
                List<Punishment> punishmentList = managementDatabase.getPunishmentPortfolioAsync(target.fetchUUID()).get().punishments();
                PunishmentLogsGUI gui = new PunishmentLogsGUI(target, punishmentList);

                Bukkit.getScheduler().runTask(plugin, () -> gui.open(sender));

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                sender.sendMessage(new InternalErrorMessage().getAsComponent());
            }
        });
    }
}
