package com.unclecole.bossrobots.objects;

import com.unclecole.bossrobots.utils.C;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.List;

public class RobotPhysical {

    Material material;
    String name;
    List<String> lore;
    boolean enchanted;
    ItemStack item;

    public RobotPhysical(Material material, String name, List<String> lore, boolean enchanted) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.enchanted = enchanted;

        ItemBuilder builder = new ItemBuilder(material).setName(C.color(name)).addLore(C.color(lore))
                .addItemFlags(ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_DESTROYS,ItemFlag.HIDE_POTION_EFFECTS);
        if(enchanted) {
            builder.addEnchant(Enchantment.DIG_SPEED,1);
        }
        this.item = builder;
    }

    public ItemStack getRobot() {
        return item;
    }
}
