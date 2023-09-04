package me.skinnynoonie.nooniemanagement.config.messages;

import me.skinnynoonie.nooniemanagement.config.ConfigurableMessage;
import me.skinnynoonie.nooniemanagement.config.DefaultMessageConfigValue;
import me.skinnynoonie.nooniemanagement.util.NameableUser;

@DefaultMessageConfigValue(defaultValue = "<aqua>{target}<gray> has been <white>unmuted<gray> by <aqua>{issuer}<gray>.")
public class PublicUnmuteAnnouncement extends ConfigurableMessage {

    private final NameableUser target;
    private final NameableUser issuer;

    public PublicUnmuteAnnouncement(NameableUser target, NameableUser issuer) {
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