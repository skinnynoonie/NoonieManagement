package me.skinnynoonie.nooniemanagement.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.skinnynoonie.nooniemanagement.config.messages.InternalErrorMessage;
import me.skinnynoonie.nooniemanagement.config.messages.MuteReminderMessage;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.BroadcastMessageEvent;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public record ChatListener(ManagementDatabase managementDatabase) implements Listener {

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) throws RuntimeException {
        UUID chatterUUID = event.getPlayer().getUniqueId();
        try {
            PunishmentPortfolio portfolio = managementDatabase.getPunishmentPortfolioAsync(chatterUUID).get();
            cancelAndRemindIfMuted(portfolio, event);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            event.setCancelled(true);
            event.getPlayer().sendMessage(new InternalErrorMessage().getAsComponent());
        }
    }

    @EventHandler
    public void onBroadcast(BroadcastMessageEvent event) {
        boolean consoleDidNotReceiveMessage = !event.getRecipients().contains(Bukkit.getConsoleSender());
        if(consoleDidNotReceiveMessage) {
            Bukkit.getConsoleSender().sendMessage(event.message());
        }
    }

    private void cancelAndRemindIfMuted(PunishmentPortfolio portfolio, AsyncChatEvent event) {
        Punishment possibleMute = portfolio.getCurrentMute();
        boolean isNotMuted = possibleMute == null;
        if(isNotMuted) return;
        event.getPlayer().sendMessage(new MuteReminderMessage(possibleMute).getAsComponent());
        event.setCancelled(true);
    }

}
