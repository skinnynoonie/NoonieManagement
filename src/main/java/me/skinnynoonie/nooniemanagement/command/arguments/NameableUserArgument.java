package me.skinnynoonie.nooniemanagement.command.arguments;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NameableUserArgument {

    public static Argument<NameableUser> get(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            String input = info.input();
            return getNameableUser(input);
        }).replaceSuggestions(getOnlinePlayerSuggestions());
    }

    private static NameableUser getNameableUser(String input) throws CustomArgument.CustomArgumentException {
        try {
            return NameableUser.parse(input);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw CustomArgument.CustomArgumentException.fromString("Unknown username/uuid. Input: '"+input+"'.");
        }
    }

    private static ArgumentSuggestions<CommandSender> getOnlinePlayerSuggestions() {
        return ArgumentSuggestions.strings(
                (info) -> Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .toArray(String[]::new));
    }

}
