package me.skinnynoonie.nooniemanagement.config.messages.gui;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<gray>Previous")
public record PreviousPageName() implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
