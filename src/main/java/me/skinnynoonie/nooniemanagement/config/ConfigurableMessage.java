package me.skinnynoonie.nooniemanagement.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;

public abstract class ConfigurableMessage {

    public static final HashMap<Class<? extends ConfigurableMessage>, String> MESSAGE_VALUES = new HashMap<>();

    protected abstract String getFormatted();

    public Component getAsComponent() {
        return MiniMessage.miniMessage().deserialize(getFormatted());
    }

    public String getAsString() {
        return getFormatted();
    }

    public String getUnformatted() {
        return MESSAGE_VALUES.get(getClass());
    }

}
