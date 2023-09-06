package me.skinnynoonie.nooniemanagement.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ComponentItemBuilder {

    private final ItemStack itemStack;

    public ComponentItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ComponentItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemStack build() {
        return itemStack;
    }

    public ComponentItemBuilder name(Component name) {
        editItemMeta(meta -> meta.displayName(name));
        return this;
    }

    public ComponentItemBuilder lore(Component lore) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        String serializedLore = miniMessage.serialize(lore);
        List<Component> componentList = Arrays.stream(serializedLore.split("\n"))
                .map(miniMessage::deserialize)
                .map(c -> c.decoration(TextDecoration.ITALIC, false))
                .toList();
        return lore(componentList);
    }

    public ComponentItemBuilder lore(List<Component> lore) {
        editItemMeta(itemMeta -> itemMeta.lore(lore));
        return this;
    }


    public ComponentItemBuilder quantity(int quantity) {
        itemStack.setAmount(quantity);
        return this;
    }

    public void editItemMeta(Consumer<ItemMeta> metaConsumer) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            metaConsumer.accept(itemMeta);
            itemStack.setItemMeta(itemMeta);
        }
    }

}
