package me.skinnynoonie.nooniemanagement.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;

public interface ConfigurableMessage {

    HashMap<Class<? extends ConfigurableMessage>, String> MESSAGE_VALUES = new HashMap<>();

    String getFormatted();

    default Component getAsComponent() {
        return MiniMessage.miniMessage().deserialize(getFormatted());
    }

    default String getAsString() {
        return getFormatted();
    }

    default String getUnformatted() {
        return MESSAGE_VALUES.get(getClass());
    }

}
