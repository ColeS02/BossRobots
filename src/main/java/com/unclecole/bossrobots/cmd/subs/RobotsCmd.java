package com.unclecole.bossrobots.cmd.subs;

import com.unclecole.bossrobots.cmd.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

public class RobotsCmd extends AbstractCommand {

    public RobotsCmd() {
        super("robots", false);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        Player player = (Player) sender;



        return false;
    }

    @Override
    public String getDescription() {
        return "Default Robots Cmd";
    }

    @Override
    public String getPermission() {
        return "bossrobots.robots";
    }
}
