package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>That player is already banned!")
public class AlreadyBannedErrorMessage implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
