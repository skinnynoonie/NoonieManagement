package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.util.NameableUser;

@DefaultMessageConfigValue(defaultValue = "{target} has been banned by {issuer}.")
public class PublicBanAnnouncement extends ConfigurableMessage {

    private final NameableUser target;
    private final NameableUser issuer;

    public PublicBanAnnouncement(NameableUser target, NameableUser issuer) {
        this.target = target;
        this.issuer = issuer;
    }

    @Override
    public String getFormatted() {
        return getUnformatted()
                .replace("{target}", target.fetchDisplayableName("?"))
                .replace("{issuer}", issuer.fetchDisplayableName("[CONSOLE]"));
    }

}
