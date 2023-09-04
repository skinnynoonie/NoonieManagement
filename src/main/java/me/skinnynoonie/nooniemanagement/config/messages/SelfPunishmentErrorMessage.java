package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;

@DefaultMessageConfigValue(defaultValue = "<red>You cannot punish yourself.")
public class SelfPunishmentErrorMessage implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted();
    }

}