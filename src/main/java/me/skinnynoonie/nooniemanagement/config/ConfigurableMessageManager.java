package me.skinnynoonie.nooniemanagement.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableMessageManager {

    private final ConfigurableMessageOrganizerImpl messageOrganizerImpl;
    private final List<Class<? extends ConfigurableMessage>> configurableMessages = new ArrayList<>();

    public ConfigurableMessageManager(ConfigurableMessageOrganizerImpl messageOrganizerImpl) {
        this.messageOrganizerImpl = messageOrganizerImpl;
    }

    public void reloadAllMessages() {
        configurableMessages.forEach(this::reload);
    }

    public void initiate() {
        try {
            messageOrganizerImpl.initiate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(Class<? extends ConfigurableMessage> configurableMessage) {
        try {
            messageOrganizerImpl.register(configurableMessage);
            configurableMessages.add(configurableMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reload(Class<? extends ConfigurableMessage> configurableMessage) {
        try {
            messageOrganizerImpl.reload(configurableMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ConfigurableMessageOrganizerImpl getMessageOrganizerImpl() {
        return this.messageOrganizerImpl;
    }

}
