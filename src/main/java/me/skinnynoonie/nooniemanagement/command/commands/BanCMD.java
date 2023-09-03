package me.skinnynoonie.nooniemanagement.command.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;
import me.skinnynoonie.nooniemanagement.command.CustomCommand;
import me.skinnynoonie.nooniemanagement.command.arguments.IndefiniteDurationArgument;
import me.skinnynoonie.nooniemanagement.command.arguments.NameableUserArgument;
import me.skinnynoonie.nooniemanagement.config.messages.*;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.permission.permissions.CommandPermissions;
import me.skinnynoonie.nooniemanagement.permission.permissions.PunishmentPermissions;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentType;
import me.skinnynoonie.nooniemanagement.util.IndefiniteDuration;
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

public record BanCMD(Plugin plugin, ManagementDatabase managementDatabase) implements ResultingCommandExecutor, CustomCommand {

    @Override
    public void register() {
        unregister();
        new CommandTree("ban")
                .withAliases("banish")
                .withUsage("/ban <player> <duration> <reason>")
                .withShortDescription("Ban a player.")
                .withFullDescription("Bans a player with reason and a specified duration. By default, durations are permanent, and reasons will be \"No reason specified.\".")
                .withPermission(CommandPermissions.BAN_COMMAND.getPermission())
                .then(new MultiLiteralArgument("silent", "-silent", "-s")
                        .then(NameableUserArgument.get("target")
                            .then(IndefiniteDurationArgument.get("duration").setOptional(true)
                                .then(new GreedyStringArgument("reason").setOptional(true)
                                        .executes(this)
                                ))))
                .then(NameableUserArgument.get("target")
                    .then(IndefiniteDurationArgument.get("duration").setOptional(true)
                        .then(new GreedyStringArgument("reason").setOptional(true)
                            .executes(this)
                        )))
                .register();
    }

    @Override
    public void unregister() {
        CommandAPI.unregister("ban");
    }

    @Override
    public int run(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        boolean silent = args.getOptional("silent").isPresent();
        NameableUser issuer = NameableUser.fromCommandSender(sender);
        NameableUser target = (NameableUser) args.get("target");
        IndefiniteDuration indefiniteDuration = (IndefiniteDuration) args.getOptional("duration").orElse(IndefiniteDuration.INFINITE);
        String reason = (String) args.getOptional("reason").orElse("No reason specified");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            target.loadAndCache();
            UUID targetUUID = target.fetchUUID();

            boolean targetIsCommandSender = PlayerUtils.isSamePerson(sender, targetUUID);
            if(targetIsCommandSender) {
                sender.sendMessage(new CannotPunishSelfMessage().getAsComponent());
                return;
            }

            boolean targetHasBanBypass = PlayerUtils.luckPermHasPermission(targetUUID, PunishmentPermissions.BYPASS_BANS.getPermission());
            if(targetHasBanBypass) {
                sender.sendMessage(new CannotPunishPlayerMessage().getAsComponent());
                return;
            }

            Punishment punishment = new Punishment.Builder()
                    .setType(PunishmentType.BAN)
                    .setIssuer(issuer)
                    .setTarget(targetUUID)
                    .setDuration(indefiniteDuration)
                    .setReason(reason)
                    .build();

            try {
                PunishmentPortfolio portfolio = managementDatabase.getPunishmentPortfolioAsync(targetUUID).get();
                boolean targetIsAlreadyBanned = portfolio.getCurrentBan() != null;
                if(targetIsAlreadyBanned) {
                    sender.sendMessage(new AlreadyBannedErrorMessage().getAsComponent());
                    return;
                }
                portfolio.punishments().add(punishment);
                managementDatabase.savePunishmentPortfolioAsync(portfolio).join();
            } catch (InterruptedException | ExecutionException e) {
                sender.sendMessage(new InternalErrorMessage().getAsComponent());
                e.printStackTrace();
                return;
            }

            kickTargetSynchronouslyIfOnline(target, punishment);

            broadcastMessageLog(silent, target, issuer);

        });
        return 1;
    }

    private void broadcastMessageLog(boolean silent, NameableUser target, NameableUser issuer) {
        if(silent) {
            Component announcement = new SilentBanAnnouncement(target, issuer).getAsComponent();
            Bukkit.broadcast(announcement, PunishmentPermissions.VIEW_SILENT_PUNISHMENTS.getPermission());
        } else {
            Component announcement = new PublicBanAnnouncement(target, issuer).getAsComponent();
            Bukkit.broadcast(announcement);
        }
    }

    private void kickTargetSynchronouslyIfOnline(NameableUser target, Punishment punishment) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player possibleOnlinePlayer = Bukkit.getPlayer(target.fetchUUID());
            if(possibleOnlinePlayer == null) return;
            possibleOnlinePlayer.kick(
                    new BanScreenMessage(punishment).getAsComponent(),
                    PlayerKickEvent.Cause.BANNED
            );
        });
    }

}
