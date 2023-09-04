package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "No reason specified")
public class DefaultPunishmentReason extends ConfigurableMessage {

    @Override
    protected String getFormatted() {
        return getUnformatted();
    }

}
