package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>That player is not muted!")
public class NotMutedErrorMessage extends ConfigurableMessage {

    @Override
    protected String getFormatted() {
        return getUnformatted();
    }

}
