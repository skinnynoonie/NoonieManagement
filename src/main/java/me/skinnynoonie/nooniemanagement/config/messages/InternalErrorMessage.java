package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>An internal error has occurred while attempting this task.")
public class InternalErrorMessage implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
