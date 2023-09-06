package me.skinnynoonie.nooniemanagement.guis.toolkits;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.skinnynoonie.nooniemanagement.config.messages.gui.NextPageName;
import me.skinnynoonie.nooniemanagement.config.messages.gui.PreviousPageName;
import me.skinnynoonie.nooniemanagement.config.messages.gui.PunishmentInfoLore;
import me.skinnynoonie.nooniemanagement.config.messages.gui.PunishmentInfoName;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;
import me.skinnynoonie.nooniemanagement.util.ComponentItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PunishLogsGUIToolKit {

    public ItemStack getSkullInfo(PlayerProfile targetProfile, List<Punishment> punishments) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        skull.editMeta(SkullMeta.class, meta -> meta.setPlayerProfile(targetProfile));
        return skull;
    }

    public ItemStack getPunishmentInfo(Punishment punishment, HashMap<UUID, String> uuidToUsernameCache) {
        return new ComponentItemBuilder(Material.MAP)
                .name(new PunishmentInfoName(punishment).getAsComponent().decoration(TextDecoration.ITALIC, false))
                .lore(new PunishmentInfoLore(punishment, uuidToUsernameCache).getAsComponent())
                .build();
    }

    public ItemStack getPreviousPageItem() {
        return new ComponentItemBuilder(Material.ARROW)
                .name(new PreviousPageName().getAsComponent())
                .build();
    }
    
    public ItemStack getNextPageItem() {
        return new ComponentItemBuilder(Material.ARROW)
                .name(new NextPageName().getAsComponent())
                .build();
    }
    
    public ItemStack getFillerHeader() {
        return new ComponentItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name(Component.text("<red>"))
                .build();
    }

    public ItemStack getFillerBody() {
        return new ComponentItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.text("<red>"))
                .build();
    }

}
