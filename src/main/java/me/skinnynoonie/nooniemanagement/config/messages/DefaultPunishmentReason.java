package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "No reason specified")
public class DefaultPunishmentReason implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}
