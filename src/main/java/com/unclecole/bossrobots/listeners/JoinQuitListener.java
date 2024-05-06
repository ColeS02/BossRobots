package com.unclecole.bossrobots.listeners;

import com.unclecole.bossrobots.database.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerData.load(event.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerData.save(event.getPlayer().getUniqueId().toString());
        PlayerData.playerRobots.remove(event.getPlayer().getUniqueId()); }


}
