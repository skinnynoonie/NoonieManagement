package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>An internal error has occurred while attempting this task.")
public class InternalErrorMessage extends ConfigurableMessage {

    @Override
    protected String getFormatted() {
        return getUnformatted();
    }

}
