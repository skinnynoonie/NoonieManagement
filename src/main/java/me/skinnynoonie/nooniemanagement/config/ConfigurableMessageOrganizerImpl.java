package me.skinnynoonie.nooniemanagement.config;

public interface ConfigurableMessageOrganizerImpl {

    void initiate() throws Exception;

    void register(Class<? extends ConfigurableMessage> configurableMessage) throws Exception;

    void reload(Class<? extends ConfigurableMessage> configurableMessage) throws Exception;

}
