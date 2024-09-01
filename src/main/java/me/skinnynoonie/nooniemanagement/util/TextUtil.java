package me.skinnynoonie.nooniemanagement.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class TextUtil {
    public static Component text(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }
}
