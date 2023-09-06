package me.skinnynoonie.nooniemanagement.config.messages.chat;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue =  "<red>You cannot punish this player!")
public record CannotPunishPlayerMessage() implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
