package me.skinnynoonie.nooniemanagement.listeners;

import me.skinnynoonie.nooniemanagement.config.messages.chat.BanScreenMessage;
import me.skinnynoonie.nooniemanagement.config.messages.chat.InternalErrorMessage;
import me.skinnynoonie.nooniemanagement.database.ManagementDatabase;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.punishment.PunishmentPortfolio;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public record PlayerConnectionListener(ManagementDatabase managementDatabase) implements Listener {

    @EventHandler
    public void onPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        try {
            PunishmentPortfolio portfolio = managementDatabase.getPunishmentPortfolioAsync(event.getUniqueId()).get();
            handlePlayerIfBanned(portfolio, event);
        } catch (Exception e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, new InternalErrorMessage().getAsComponent());
        }
    }

    private void handlePlayerIfBanned(PunishmentPortfolio portfolio, AsyncPlayerPreLoginEvent event) {
        Punishment possibleCurrentBan = portfolio.getCurrentBan();
        boolean isNotBanned = possibleCurrentBan == null;
        if(isNotBanned) return;
        kickPlayerDueToBan(possibleCurrentBan, event);
    }

    private void kickPlayerDueToBan(Punishment banPunishment, AsyncPlayerPreLoginEvent event) {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                new BanScreenMessage(banPunishment).getAsComponent());
    }

}
