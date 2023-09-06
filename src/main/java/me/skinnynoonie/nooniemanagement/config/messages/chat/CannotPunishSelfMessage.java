package me.skinnynoonie.nooniemanagement.config.messages.chat;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>You cannot punish yourself!")
public record CannotPunishSelfMessage() implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
