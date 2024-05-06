package com.unclecole.bossrobots.listeners;

import com.unclecole.bossrobots.BossRobots;
import com.unclecole.bossrobots.database.PlayerData;
import com.unclecole.bossrobots.database.RobotData;
import com.unclecole.bossrobots.objects.RobotTypes;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class RobotBreakListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        String loc = BossRobots.getInstance().getLocationUtility().parseToString(event.getBlock().getLocation().clone().subtract(-0.5,0.0,-0.5));
        BossRobots.getInstance().getRobotConfig().forEach((s, robotTypes) -> {
            if(event.getBlock().getType().equals(robotTypes.getArmorStand().getMineBlock())) {
                RobotData.robotLocations.forEach((location, location2) -> {
                    if(loc.equals(location2)) {
                        event.setCancelled(true);
                    }
                });
            }
        });
    }

    @EventHandler
    public void onBreak(EntityDamageByEntityEvent event) {
        if(!event.getEntity().getType().equals(EntityType.ARMOR_STAND)) return;
        if(!(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        Player player = ((Player) event.getDamager()).getPlayer();
        assert player != null;
        if(player.isSneaking()) {
            if(!RobotData.robotLocations.containsKey(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation()))) return;
            event.setCancelled(true);

            Location location = BossRobots.getInstance().getLocationUtility().parseToLocation(RobotData.robotLocations.get(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation())));

            location.getBlock().setType(Material.AIR);
            Location robotArmorStand = event.getEntity().getLocation();

            robotArmorStand.getWorld().getNearbyEntities(robotArmorStand, 0.5,0.5,0.5).forEach(entity ->{
                if(entity.getType().equals(EntityType.ARMOR_STAND)) {
                    entity.remove();
                }
            });

            UUID uuid = UUID.fromString(RobotData.robotList.get((BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation()))));

            String value = PlayerData.playerRobots.get(uuid).get(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation())).getFortuneLvl()
                    + ":"
                    + PlayerData.playerRobots.get(uuid).get(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation())).getEffLvl();

            RobotTypes robotTypes = BossRobots.getInstance().getRobotConfig().get(PlayerData.playerRobots.get(uuid).get(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation())).getType());

            RobotData.robotList.remove(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation()));
            RobotData.robotLocations.remove(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation()));
            PlayerData.playerRobots.get(uuid).remove(BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation()));
            player.closeInventory();
            ItemStack robot = robotTypes.getRobot();
            NBTItem robotNBT = new NBTItem(robot);
            robotNBT.setString("level", value);

            robot = robotNBT.getItem();

            player.getInventory().addItem(robot);
            PlayerData.save(uuid.toString());
        }
        String loc = BossRobots.getInstance().getLocationUtility().parseToString(event.getEntity().getLocation());

        RobotData.robotLocations.forEach((location, location2) -> {
            if(loc.equals(location)) {
                event.setCancelled(true);
                PlayerData.playerRobots.get(event.getDamager().getUniqueId()).forEach((s, robot) -> {
                    if(location.equals(s)) {
                        PlaceRobot.openMenu(((Player) event.getDamager()).getPlayer(), robot);
                    }
                });
            }
        });
    }
}
