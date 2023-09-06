package me.skinnynoonie.nooniemanagement.config.messages.chat;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "No reason specified")
public record DefaultPunishmentReason() implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
