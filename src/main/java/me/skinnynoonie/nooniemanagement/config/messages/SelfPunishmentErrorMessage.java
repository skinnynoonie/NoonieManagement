package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>You cannot punish yourself.")
public class SelfPunishmentErrorMessage extends ConfigurableMessage {

    @Override
    protected String getFormatted() {
        return getUnformatted();
    }

}