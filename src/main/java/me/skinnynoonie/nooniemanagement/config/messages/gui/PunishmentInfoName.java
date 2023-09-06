package me.skinnynoonie.nooniemanagement.config.messages.gui;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.punishment.Punishment;

@DefaultMessageConfigValue(defaultValue = "<aqua>{type}")
public record PunishmentInfoName(Punishment punishment) implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted()
                .replace("{type}", punishment.getPunishmentType().toString().toUpperCase());
    }

}
