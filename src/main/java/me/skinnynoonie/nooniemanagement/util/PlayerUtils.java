package me.skinnynoonie.nooniemanagement.util;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtils {

    public static boolean luckPermHasPermission(UUID uuid, String permission) {
        Player player = Bukkit.getPlayer(uuid);
        if(player != null) return player.hasPermission(permission);

        User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
        if(user == null) return false;

        return user.getCachedData()
                .getPermissionData()
                .checkPermission(permission)
                .asBoolean();
    }

    public static boolean isSamePerson(CommandSender sender, UUID uuid) {
        if(sender == null || uuid == null) return false;
        if(sender instanceof Player p) {
            return p.getUniqueId().equals(uuid);
        }
        return false;
    }


}
