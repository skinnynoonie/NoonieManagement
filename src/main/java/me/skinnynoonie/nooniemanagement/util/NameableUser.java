package me.skinnynoonie.nooniemanagement.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * May create a blocking web request to get info if not cached already.
 */
public final class NameableUser {
    
    // todo: Make these a Pattern, so you match with Pattern#matcher or something like that.
    private final static Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private final static Pattern MINECRAFT_USERNAME_PATTERN = Pattern.compile("^\\w{2,16}$");

    public final static NameableUser UNKNOWN = new NameableUser(null, null);

    public static NameableUser parse(String contentToParse) {
        Preconditions.checkNotNull(contentToParse, "Content to parse cannot be null!");
        if(UUID_PATTERN.matcher(contentToParse).matches()) {
            return new NameableUser(UUID.fromString(contentToParse), null);
        }
        if(MINECRAFT_USERNAME_PATTERN.matcher(contentToParse).matches()) {
            return new NameableUser(null, contentToParse);
        }
        throw new IllegalArgumentException("Invalid argument, could not be parsed! Content to parse: '"+contentToParse+"'.");
    }

    public static NameableUser fromCommandSender(CommandSender commandSender) {
        Preconditions.checkNotNull(commandSender, "Command sender cannot be null!");
        if(commandSender instanceof Player player) {
            return new NameableUser(player.getUniqueId(), player.getName());
        }
        return NameableUser.UNKNOWN;
    }

    public static NameableUser fromUUID(UUID uuid) {
        if(uuid == null) {
            return UNKNOWN;
        }
        return new NameableUser(uuid, null);
    }

    public static NameableUser fromUsername(String username) {
        Preconditions.checkNotNull(username, "Username cannot be null!");
        Preconditions.checkState(MINECRAFT_USERNAME_PATTERN.matcher(username).matches(), "Invalid minecraft username! Username: '"+username+"'.");
        return new NameableUser(null, username);
    }

    private UUID uuid;
    private String username;
    private boolean fetchedUsername = false;

    private NameableUser(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public String fetchDisplayableName(String unknownCase) {
        if(uuid == null && username == null) return unknownCase;
        String possibleUsername = fetchUsername();
        String displayUUID = fetchUUID().toString();

        return possibleUsername != null ? possibleUsername : displayUUID;
    }

    public void loadAndCache() {
        fetchUUID();
        fetchUsername();
    }

    public UUID fetchUUID() {
        if(uuid != null) return uuid;
        if(username == null) return null;

        return uuid = Bukkit.getOfflinePlayer(username).getUniqueId();
    }

    public String fetchUsername() {
        if(username != null) return username;
        if(uuid == null) return null;

        if(fetchedUsername) return null;
        fetchedUsername = true;
        return username = Bukkit.getOfflinePlayer(uuid).getName();
    }

    public boolean hasUUIDCached() {
        return uuid != null;
    }

    public boolean hasUsername() {
        return username != null;
    }

    @Override
    public String toString() {
        return "NameableUser{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                '}';
    }

}
