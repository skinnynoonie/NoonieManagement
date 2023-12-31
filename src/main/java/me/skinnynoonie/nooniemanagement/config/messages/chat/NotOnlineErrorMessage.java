package me.skinnynoonie.nooniemanagement.config.messages.chat;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>That player is not online!")
public record NotOnlineErrorMessage() implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
