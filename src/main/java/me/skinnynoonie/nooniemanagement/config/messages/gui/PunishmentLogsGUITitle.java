package me.skinnynoonie.nooniemanagement.config.messages.gui;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.util.NameableUser;

@DefaultMessageConfigValue(defaultValue = "<gray>Punishment Logs: {player}")
public record PunishmentLogsGUITitle(NameableUser target) implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted()
                .replace("{player}", target.fetchDisplayableName("?"));
    }

}
