package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;

@DefaultMessageConfigValue(defaultValue =
        """
        <gray>You have been warned!
        
        <white>Reason: <aqua>{reason}
        """)
public record WarnReminderMessage(Punishment punishment) implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted()
                .replace("{reason}", punishment.getReason());
    }

}
