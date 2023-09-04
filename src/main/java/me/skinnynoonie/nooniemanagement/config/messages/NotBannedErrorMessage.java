package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>That player is not banned!")
public class NotBannedErrorMessage implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
