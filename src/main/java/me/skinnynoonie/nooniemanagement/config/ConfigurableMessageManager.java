package me.skinnynoonie.nooniemanagement.config;

import me.skinnynoonie.nooniemanagement.config.messages.InternalErrorMessage;
import me.skinnynoonie.nooniemanagement.config.messages.PublicBanAnnouncement;

import java.util.List;

// todo: move registration of this plugin's messages outside this class, like into main or a new class. it promotes this class being more concrete.
public class ConfigurableMessageManager {

    private final ConfigurableMessageOrganizerImpl messageOrganizerImpl;
    private final List<Class<? extends ConfigurableMessage>> configurableMessages = getConfigurableMessages();

    public ConfigurableMessageManager(ConfigurableMessageOrganizerImpl messageOrganizerImpl) {
        this.messageOrganizerImpl = messageOrganizerImpl;
    }

    public void registerAllMessages() {
        configurableMessages.forEach(this::register);
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

    private List<Class<? extends ConfigurableMessage>> getConfigurableMessages() {
        return List.of(
                PublicBanAnnouncement.class,
                InternalErrorMessage.class
        );
    }

}
