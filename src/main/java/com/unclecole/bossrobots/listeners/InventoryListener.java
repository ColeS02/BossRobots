package com.unclecole.bossrobots.listeners;

import com.unclecole.bossrobots.BossRobots;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if(BossRobots.getInstance().getActiveMenuPlayers().contains(uuid)) {
            BossRobots.getInstance().getActiveMenuPlayers().remove(uuid);
        }
    }
}
