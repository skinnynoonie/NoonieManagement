package me.skinnynoonie.nooniemanagement.command.arguments;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.skinnynoonie.nooniemanagement.util.IndefiniteDuration;
import org.bukkit.command.CommandSender;

public class IndefiniteDurationArgument {

    private final static String PERMANENT_DURATION = "(perm)|(permanent)";

    public static Argument<IndefiniteDuration> get(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            String input = info.input();
            return getIndefiniteDuration(input);
        }
        ).replaceSuggestions(getIndefiniteDurationSuggestions());
    }

    private static ArgumentSuggestions<CommandSender> getIndefiniteDurationSuggestions() {
        String[] suggestions = {
                "perm",
                "permanent",
                "30d",
                "10d12hr"
        };
        return ArgumentSuggestions.strings(suggestions);
    }

    private static IndefiniteDuration getIndefiniteDuration(String input) throws CustomArgument.CustomArgumentException {
        input = input.toLowerCase();
        if(input.matches(PERMANENT_DURATION)) {
            return IndefiniteDuration.INFINITE;
        }
        return getHandledIndefiniteDuration(input);
    }

    private static IndefiniteDuration getHandledIndefiniteDuration(String input) throws CustomArgument.CustomArgumentException {
        IndefiniteDuration indefiniteDuration;
        try {
            indefiniteDuration = IndefiniteDuration.parse(input);
        } catch (NullPointerException e) {
            indefiniteDuration = IndefiniteDuration.from(0);
        }
        if(indefiniteDuration.getMillis() <= 0) {
            throw CustomArgument.CustomArgumentException.fromString("Invalid duration! Input: '"+input+"'.");
        }
        return indefiniteDuration;
    }

}
