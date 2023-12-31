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
import me.skinnynoonie.nooniemanagement.config.messages.chat.*;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.permission.permissions.CommandPermissions;
import me.skinnynoonie.nooniemanagement.permission.permissions.PunishmentPermissions;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentType;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import me.skinnynoonie.nooniemanagement.util.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public record KickCMD(Plugin plugin, ManagementDatabase managementDatabase) implements CustomCommand, ResultingCommandExecutor {

    @Override
    public void register() {
        unregister();
        new CommandTree("kick")
                .withPermission(CommandPermissions.KICK_COMMAND.getPermission())
                .withShortDescription("Kicks a player")
                .withFullDescription("Kicks a player. Reason by default is \""+new DefaultPunishmentReason().getAsString()+"\".")
                .withUsage("/kick [-silent] <player> <reason>")
                .then(NameableUserArgument.get("target")
                        .then(new GreedyStringArgument("reason").setOptional(true)
                                .executes(this)
                        ))
                .then(new MultiLiteralArgument("silent", "-silent", "-s")
                        .then(NameableUserArgument.get("target")
                                .then(new GreedyStringArgument("reason").setOptional(true)
                                        .executes(this)
                                )))
                .register();
    }

    @Override
    public void unregister() {
        CommandAPI.unregister("kick");
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

            boolean targetIsCommandSender = PlayerUtils.isSamePerson(sender, targetUUID);
            if(targetIsCommandSender) {
                sender.sendMessage(new CannotPunishSelfMessage().getAsComponent());
                return;
            }

            boolean targetIsNotOnline = Bukkit.getPlayer(target.fetchUUID()) == null;
            if(targetIsNotOnline) {
                sender.sendMessage(new NotOnlineErrorMessage().getAsComponent());
                return;
            }

            boolean targetHasKickBypass = PlayerUtils.luckPermHasPermission(targetUUID, PunishmentPermissions.BYPASS_KICKS.getPermission());
            if(targetHasKickBypass) {
                sender.sendMessage(new CannotPunishPlayerMessage().getAsComponent());
                return;
            }

            Punishment punishment = new Punishment.Builder()
                    .setIssuer(issuer)
                    .setTarget(targetUUID)
                    .setReason(reason)
                    .setType(PunishmentType.KICK)
                    .build();

            try {
                PunishmentPortfolio portfolio = managementDatabase.getPunishmentPortfolioAsync(targetUUID).get();
                portfolio.punishments().add(punishment);
                managementDatabase.savePunishmentPortfolioAsync(portfolio);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                sender.sendMessage(new InternalErrorMessage().getAsComponent());
                return;
            }

            broadcastMessageLog(silent, target, issuer);

            kickTargetSynchronouslyIfOnline(target, punishment);

        });

        return 1;
    }

    private void broadcastMessageLog(boolean silent, NameableUser target, NameableUser issuer) {
        if(silent) {
            Component announcement = new SilentKickAnnouncement(target, issuer).getAsComponent();
            Bukkit.broadcast(announcement, PunishmentPermissions.VIEW_SILENT_PUNISHMENTS.getPermission());
        } else {
            Component announcement = new PublicKickAnnouncement(target, issuer).getAsComponent();
            Bukkit.broadcast(announcement);
        }
    }

    private void kickTargetSynchronouslyIfOnline(NameableUser target, Punishment punishment) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player possibleOnlinePlayer = Bukkit.getPlayer(target.fetchUUID());
            if(possibleOnlinePlayer == null) return;
            possibleOnlinePlayer.kick(
                    new KickScreenMessage(punishment).getAsComponent(),
                    PlayerKickEvent.Cause.KICK_COMMAND
            );
        });
    }

}
