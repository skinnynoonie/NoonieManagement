package me.skinnynoonie.nooniemanagement.config.messages.chat;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.util.NameableUser;

@DefaultMessageConfigValue(defaultValue = "<aqua>{target}<gray> has been <white>warned<gray> by <aqua>{issuer}<gray>.")
public record PublicWarnAnnouncement(NameableUser target, NameableUser issuer) implements ConfigurableMessage {

    @Override
    public String getFormatted() {
        return getUnformatted()
                .replace("{target}", target.fetchDisplayableName("?"))
                .replace("{issuer}", issuer.fetchDisplayableName(new ConsoleName().getAsString()));
    }

}