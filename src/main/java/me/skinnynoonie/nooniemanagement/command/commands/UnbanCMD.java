package me.skinnynoonie.nooniemanagement.command.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;
import me.skinnynoonie.nooniemanagement.command.CustomCommand;
import me.skinnynoonie.nooniemanagement.command.arguments.NameableUserArgument;
import me.skinnynoonie.nooniemanagement.config.messages.DefaultPunishmentReason;
import me.skinnynoonie.nooniemanagement.config.messages.InternalErrorMessage;
import me.skinnynoonie.nooniemanagement.config.messages.NotBannedErrorMessage;
import me.skinnynoonie.nooniemanagement.config.messages.PublicUnbanAnnouncement;
import me.skinnynoonie.nooniemanagement.config.messages.SilentUnbanAnnouncement;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.permission.permissions.CommandPermissions;
import me.skinnynoonie.nooniemanagement.permission.permissions.PunishmentPermissions;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public record UnbanCMD(Plugin plugin, ManagementDatabase managementDatabase) implements ResultingCommandExecutor, CustomCommand {

    @Override
    public void register() {
        unregister();
        new CommandTree("unban")
                .withAliases("pardon")
                .withUsage("/unban [-silent] <player> <reason>")
                .withShortDescription("Unbans a player.")
                .withFullDescription("Unbans a player with reason. Reasons will be \""
                        + new DefaultPunishmentReason().getAsString() + "\" by default.")
                .withPermission(CommandPermissions.UNBAN_COMMAND.getPermission())
                .then(new MultiLiteralArgument("silent", "-silent", "-s")
                        .then(NameableUserArgument.get("target")
                                .then(new GreedyStringArgument("reason").setOptional(true)
                                                .executes(this)
                                )))
                .then(NameableUserArgument.get("target")
                        .then(new GreedyStringArgument("reason").setOptional(true)
                                        .executes(this)
                        ))
                .register();
    }

    @Override
    public void unregister() {
        CommandAPI.unregister("unban");
    }

    @Override
    public int run(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        boolean silent = args.getOptional("silent").isPresent();
        NameableUser issuer = NameableUser.fromCommandSender(sender);
        NameableUser target = (NameableUser) args.get("target");
        String reason = (String) args.getOptional("reason").orElse(new DefaultPunishmentReason().getAsString());



        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            target.loadAndCache();
            UUID targetUUID = target.fetchUUID();

            try {
                PunishmentPortfolio portfolio = managementDatabase.getPunishmentPortfolioAsync(targetUUID).get();
                boolean targetIsNotBanned = portfolio.getCurrentBan() == null;
                if(targetIsNotBanned) {
                    sender.sendMessage(new NotBannedErrorMessage().getAsComponent());
                    return;
                }
                portfolio.getCurrentBan().pardon(issuer.fetchUUID(), reason);
                managementDatabase.savePunishmentPortfolioAsync(portfolio).join();
            } catch (InterruptedException | ExecutionException e) {
                sender.sendMessage(new InternalErrorMessage().getAsComponent());
                e.printStackTrace();
                return;
            }

            broadcastMessageLog(silent, target, issuer);

        });
        return 1;
    }

    private void broadcastMessageLog(boolean silent, NameableUser target, NameableUser issuer) {
        if(silent) {
            Component announcement = new SilentUnbanAnnouncement(target, issuer).getAsComponent();
            Bukkit.broadcast(announcement, PunishmentPermissions.VIEW_SILENT_PUNISHMENTS.getPermission());
        } else {
            Component announcement = new PublicUnbanAnnouncement(target, issuer).getAsComponent();
            Bukkit.broadcast(announcement);
        }
    }
}
