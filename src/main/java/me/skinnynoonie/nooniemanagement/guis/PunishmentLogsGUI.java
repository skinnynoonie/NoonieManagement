package me.skinnynoonie.nooniemanagement.guis;

import com.destroystokyo.paper.profile.PlayerProfile;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import me.skinnynoonie.nooniemanagement.config.messages.gui.PunishmentLogsGUITitle;
import me.skinnynoonie.nooniemanagement.guis.toolkits.PunishLogsGUIToolKit;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PunishmentLogsGUI extends FastInv {

    private final PlayerProfile target;
    private final List<Punishment> punishments;
    private final HashMap<UUID, String> uuidToDisplayName = new HashMap<>();
    private final PunishLogsGUIToolKit logsGUIToolKit = new PunishLogsGUIToolKit();

    private int page = 1;

    public PunishmentLogsGUI(NameableUser target, List<Punishment> punishments) {
        super(invHolder -> Bukkit.createInventory(invHolder, 54, new PunishmentLogsGUITitle(target).getAsComponent()));
        this.target = Bukkit.getOfflinePlayer(target.fetchUUID()).getPlayerProfile();
        this.target.complete();
        this.punishments = punishments;
        uuidToDisplayName.put(target.fetchUUID(), target.fetchDisplayableName("?"));
        getAllDisplayNamesAndCache();
    }

    private void getAllDisplayNamesAndCache() {
        for(Punishment punishment : punishments) {
            NameableUser issuer = punishment.getIssuer();
            NameableUser pardoner = punishment.getPardoner();
            if(issuer.hasUUIDCached()) {
                uuidToDisplayName.put(issuer.fetchUUID(), issuer.fetchDisplayableName("?")); //Default value should not be possible.
            }
            if(pardoner != null && pardoner.hasUUIDCached()) {
                uuidToDisplayName.put(pardoner.fetchUUID(), pardoner.fetchDisplayableName("?"));
            }
        }
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {
        reRenderView();
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    private void reRenderView() {
        fillViewWithPanes();
        addHeaderBarButtons();
        fillWithPunishmentInfo();
    }

    private void fillViewWithPanes() {
        ItemStack fillerPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
        for(int i = 0; i < 54; i++) {
            setItem(i, fillerPane);
        }
    }

    private void addHeaderBarButtons() {
        if(page > 1) {
            setItem(0, logsGUIToolKit.getPreviousPageItem(), (event) -> {
                page--;
                reRenderView();
            });
        }

        setItem(4, logsGUIToolKit.getSkullInfo(target, punishments));

        if(hasNextPage()) {
            setItem(8, logsGUIToolKit.getNextPageItem(), (event) -> {
                page++;
                reRenderView();
            });
        }
    }

    private void fillWithPunishmentInfo() {
        int index = getStartingIndexOfCurrentPage();
        int listSize = punishments.size();
        for(int i = 9; i < 45; i++) {
            if(listSize <= index) return;
            setItem(i, logsGUIToolKit.getPunishmentInfo(punishments.get(index), uuidToDisplayName));
            index++;
        }
    }

    private boolean hasNextPage() {
        int displayPerPage = 4 * 9;
        int indexOfLastPunishment = punishments.size() - 1;
        int startingIndexOfNextPage = page * displayPerPage;

        return startingIndexOfNextPage <= indexOfLastPunishment;
    }

    private int getStartingIndexOfCurrentPage() {
        int displayPerPage = 4 * 9;
        return (page - 1) * displayPerPage;
    }

}
