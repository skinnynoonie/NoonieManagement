package me.skinnynoonie.nooniemanagement.util;

import org.bukkit.Bukkit;

import java.util.UUID;

public final class PlayerUtil {
    public static String getOfflineName(UUID uuid) {
        if (uuid == null) {
            return null;
        } else {
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            return name != null ? name : uuid.toString();
        }
    }

    private PlayerUtil() {
    }
}
