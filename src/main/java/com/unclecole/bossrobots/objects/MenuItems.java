package com.unclecole.bossrobots.objects;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuItems {
    String type;
    @Getter String name;
    @Getter List<String> lore;
    int slot;
    ItemStack item;

    public MenuItems(String type, int slot, String name, List<String> lore, ItemStack item) {
        this.type = type;
        this.slot = slot;
        this.name = name;
        this.lore = lore;
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}
