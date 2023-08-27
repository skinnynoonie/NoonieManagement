package me.skinnynoonie.nooniemanagement.command.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;
import me.skinnynoonie.nooniemanagement.command.PunishmentCommand;
import me.skinnynoonie.nooniemanagement.command.arguments.IndefiniteDurationArgument;
import me.skinnynoonie.nooniemanagement.command.arguments.NameableUserArgument;
import me.skinnynoonie.nooniemanagement.config.messages.InternalErrorMessage;
import me.skinnynoonie.nooniemanagement.config.messages.PublicBanAnnouncement;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentType;
import me.skinnynoonie.nooniemanagement.util.IndefiniteDuration;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class BanCMD extends PunishmentCommand implements ResultingCommandExecutor,  {

    public BanCMD(Plugin plugin, ManagementDatabase managementDatabase) {
        super(plugin, managementDatabase);
    }

    @Override
    public void register() {
        new CommandTree("ban")
                .withAliases("banish")
                .withUsage("/ban <player> <duration> <reason>")
                .withShortDescription("Ban a player.")
                .withFullDescription("Bans a player with reason and a specified duration. By default, durations are permanent, and reasons will be \"No reason specified.\".")
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
        NameableUser issuer = NameableUser.parse(sender);
        NameableUser target = (NameableUser) args.get("target");
        IndefiniteDuration indefiniteDuration = (IndefiniteDuration) args.getOptional("duration").orElse(IndefiniteDuration.INFINITE);
        String reason = (String) args.getOptional("reason").orElse("No reason specified.");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            target.loadAndCache();
            UUID targetUUID = target.fetchUUID();

            Punishment punishment = new Punishment.Builder()
                    .setType(PunishmentType.BAN)
                    .setIssuer(issuer)
                    .setTarget(targetUUID)
                    .setDuration(indefiniteDuration)
                    .setReason(reason)
                    .build();

            try {
                PunishmentPortfolio portfolio = managementDatabase.getPunishmentPortfolioAsync(targetUUID).get();
                portfolio.punishments().add(punishment);
                managementDatabase.savePunishmentPortfolioAsync(portfolio).join();
            } catch (InterruptedException | ExecutionException e) {
                sender.sendMessage(new InternalErrorMessage().getAsComponent());
                e.printStackTrace();
                return;
            }

            // todo: Add permissions for silent stuff
            if(!silent) {
                Bukkit.broadcast(new PublicBanAnnouncement(target, issuer).getAsComponent());
            }

        });
        return 1;
    }
}
