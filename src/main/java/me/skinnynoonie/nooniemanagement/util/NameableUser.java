package me.skinnynoonie.nooniemanagement.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * May create a blocking web request to get info if not cached already.
 */
public final class NameableUser {

    private final static String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private final static String MINECRAFT_USERNAME_PATTERN = "^\\w{2,16}$";

    public final static NameableUser UNKNOWN = new NameableUser();

    public static NameableUser parse(String contentToParse) {
        Preconditions.checkNotNull(contentToParse, "Content to parse cannot be null!");
        if(contentToParse.matches(UUID_PATTERN)) {
            return new NameableUser(UUID.fromString(contentToParse));
        }
        if(contentToParse.matches(MINECRAFT_USERNAME_PATTERN)) {
            return new NameableUser(contentToParse);
        }
        throw new IllegalArgumentException("Invalid argument, could not be parsed!");
    }

    private UUID uuid;
    private String username;

    public NameableUser(UUID uuid) {
        Preconditions.checkNotNull(uuid, "UUID cannot be null!");
        this.uuid = uuid;
    }

    public NameableUser(String username) {
        Preconditions.checkNotNull(username, "Username cannot be null!");
        Preconditions.checkState(username.matches(MINECRAFT_USERNAME_PATTERN), "Invalid minecraft username!");
        this.username = username;
    }

    private NameableUser() {
        //Made specifically for a completely unknown named user.
    }

    public String fetchDisplayableName(String unknownCase) {
        if(uuid == null && username == null) return unknownCase;
        String possibleUsername = fetchUsername();
        String displayUUID = fetchUUID().toString();

        return possibleUsername != null ? possibleUsername : displayUUID;
    }

    public UUID fetchUUID() {
        if(uuid != null) return uuid;
        if(username == null) return null;

        return Bukkit.getOfflinePlayer(username).getUniqueId();
    }

    public String fetchUsername() {
        if(username != null) return username;
        if(uuid == null) return null;

        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public boolean hasUUID() {
        return uuid != null;
    }

    public boolean hasUsername() {
        return username != null;
    }

}
