package me.skinnynoonie.nooniemanagement.config.messages.chat;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;

@DefaultMessageConfigValue(defaultValue =
        """
        <gray>You have been kicked!
        
        <white>Reason: <aqua>{reason}
        """)
public record KickScreenMessage(Punishment punishment) implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted()
                .replace("{reason}", punishment.getReason());
    }

}
