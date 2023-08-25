package me.skinnynoonie.nooniemanagement.command.arguments;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.skinnynoonie.nooniemanagement.util.NameableUser;
import org.bukkit.Bukkit;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NameableUserArgument extends CustomArgument<NameableUser, String> {

    private final static Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private final static Pattern MINECRAFT_USERNAME_PATTERN = Pattern.compile("^\\w{2,16}$");

    public NameableUserArgument(String nodeName) {
        super(new StringArgument(nodeName), info -> {
            String input = info.input();
            return getNameableUser(input);
        });
        //replaceSuggestions(ArgumentSuggestions.strings(suggestions -> Bukkit.getOnlinePlayers()))
    }

    private static NameableUser getNameableUser(String input) throws CustomArgumentException {
        try {
            return NameableUser.parse(input);
        } catch (IllegalArgumentException e) {
            throw CustomArgumentException.fromString("Unknown username/uuid.");
        }
    }


}
