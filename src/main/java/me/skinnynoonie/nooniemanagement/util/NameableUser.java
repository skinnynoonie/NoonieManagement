package me.skinnynoonie.nooniemanagement.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;

import java.util.UUID;

public record NameableUser(UUID assignedUUID) {

    public static final NameableUser UNKNOWN = new NameableUser(null);

    /**
     * Creates a blocking web request.
     */
    public static NameableUser fromUsername(String playerUsername) {
        Preconditions.checkNotNull(playerUsername, "Player username cannot be null!");
        UUID uuid = Bukkit.getOfflinePlayer(playerUsername).getUniqueId();
        return new NameableUser(uuid);
    }

    /**
     * May create a blocking web request.
     */
    public String getDisplayableName(String unknownCase) {
        if (assignedUUID == null) {
            return unknownCase;
        }
        String possibleUsername = Bukkit.getOfflinePlayer(assignedUUID).getName();
        return possibleUsername == null ? assignedUUID.toString() : possibleUsername;
    }

    public boolean hasUnknownUUID() {
        return assignedUUID == null;
    }

    public boolean hasAssignedUUID() {
        return assignedUUID != null;
    }

}
