package com.unclecole.bossrobots.listeners;

import com.unclecole.bossrobots.BossRobots;
import com.unclecole.bossrobots.database.PlayerData;
import com.unclecole.bossrobots.database.RobotData;
import com.unclecole.bossrobots.objects.Robot;
import com.unclecole.bossrobots.objects.RobotTypes;
import com.unclecole.bossrobots.utilities.LocationUtility;
import com.unclecole.bossrobots.utils.C;
import com.unclecole.bossrobots.utils.NumberFormat;
import com.unclecole.bossrobots.utils.PlaceHolder;
import com.unclecole.bossrobots.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import me.taleeko.bossenchants.BossEnchants;
import me.taleeko.bossenchants.api.TokenAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;
import redempt.redlib.blockdata.custom.CustomBlock;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class PlaceRobot implements Listener {

    static BossEnchants bossEnchants = BossEnchants.getInstance();
    static TokenAPI api = new TokenAPI(bossEnchants);
    private LocationUtility locationUtility = BossRobots.getInstance().getLocationUtility();

    @EventHandler
    public void onRobotPlacement(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            BossRobots.getInstance().getRobotConfig().forEach((name, robot) -> {
                if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(robot.getRobot().getType())) {
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        placeRobot(event.getPlayer(), name, robot, event.getClickedBlock().getLocation(), event.getPlayer().getInventory().getItemInMainHand());
                    }
                    event.setCancelled(true);
                }
            });
        }
    }

    public void placeRobot(Player player, String type, RobotTypes robotTypes, Location location, ItemStack itemStack) {
        Location loc = location.clone().add(0.5, 1.0, 0.5);
        loc.setDirection(player.getFacing().getOppositeFace().getDirection());
        Location block = loc.clone();
        if (loc.getDirection().equals(BlockFace.NORTH.getDirection())) {
            block.add(0, 0, -1.0);
        } else if (loc.getDirection().equals(BlockFace.SOUTH.getDirection())) {
            block.add(0, 0, 1.0);
        } else if (loc.getDirection().equals(BlockFace.EAST.getDirection())) {
            block.add(1.0, 0, 0);
        } else if (loc.getDirection().equals(BlockFace.WEST.getDirection())) {
            block.add(-1.0, 0, 0);
        }

        if (!block.getBlock().getType().equals(Material.AIR) || !block.getWorld().getNearbyEntities(block, 0.5,0.5,0.5).isEmpty()) {
            TL.CANT_PLACE_MINION.send(player);
            return;
        }
        if(!BossRobots.getInstance().getAllowedWorlds().contains(location.getWorld().getName())) {
            TL.CANT_PLACE_MINION.send(player);
            return;
        }

        RobotData.robotList.put(BossRobots.getInstance().getLocationUtility().parseToString(loc), player.getUniqueId().toString());
        RobotData.robotLocations.put(BossRobots.getInstance().getLocationUtility().parseToString(loc), BossRobots.getInstance().getLocationUtility().parseToString(block));

        PlayerData.playerRobots.get(player.getUniqueId()).put(BossRobots.getInstance().getLocationUtility().parseToString(loc), new Robot(type, player.getUniqueId().toString(),
                BossRobots.getInstance().getLocationUtility().parseToString(loc),
                BossRobots.getInstance().getLocationUtility().parseToString(block)));

        NBTItem nbtItem = new NBTItem(itemStack);
        if(nbtItem.hasKey("level")) {
            String value = nbtItem.getString("level");
            String[] data = value.split(":");
            try {
                int fortune = Integer.parseInt(data[0]);
                int eff = Integer.parseInt(data[1]);
                PlayerData.playerRobots.get(player.getUniqueId()).get(BossRobots.getInstance().getLocationUtility().parseToString(loc)).setFortuneLvl(fortune);
                PlayerData.playerRobots.get(player.getUniqueId()).get(BossRobots.getInstance().getLocationUtility().parseToString(loc)).setEffLvl(eff);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        PlayerData.save(player.getUniqueId().toString());

        ArmorStand stand = (ArmorStand) loc.getChunk().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        stand.setSmall(true);
        stand.setSilent(true);
        stand.setBasePlate(false);
        stand.setArms(true);
        stand.setRightArmPose(new EulerAngle(-1.0, 0.0, 0.0));
        stand.getEquipment().setHelmet(new ItemStack(robotTypes.getArmorStand().getHelmet()));
        stand.getEquipment().setChestplate(new ItemStack(robotTypes.getArmorStand().getChestplate()));
        stand.getEquipment().setLeggings(new ItemStack(robotTypes.getArmorStand().getLeggings()));
        stand.getEquipment().setBoots(new ItemStack(robotTypes.getArmorStand().getBoots()));
        stand.getEquipment().setItemInMainHand(new ItemStack(robotTypes.getArmorStand().getInHand()));
        stand.setGravity(false);
        stand.setCustomName(C.color(robotTypes.getArmorStand().getName()));
        stand.setCustomNameVisible(true);
        block.getBlock().setType(robotTypes.getArmorStand().getMineBlock());
        itemStack.setAmount(itemStack.getAmount()-1);
    }

    @EventHandler
    public void onArmorStandInteract(PlayerInteractAtEntityEvent event) {
        if(!event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) return;

        Location loc = event.getRightClicked().getLocation();
        Player player = event.getPlayer();

        if(RobotData.robotList.containsKey(locationUtility.parseToString(loc))) {
            event.setCancelled(true);
            if(PlayerData.playerRobots.get(event.getPlayer().getUniqueId()).containsKey(locationUtility.parseToString(loc)) || player.hasPermission("BossRobots.admin")) {
                openMenu(event.getPlayer(), BossRobots.getInstance().getRobotFromLocation(loc));
            }
        }
    }

    public static void openMenu(Player player, Robot robot) {

        BossRobots.getInstance().getActiveMenuPlayers().add(player.getUniqueId());

        InventoryGUI menu = new InventoryGUI(BossRobots.getInstance().getRobotMenuSize(), C.color(BossRobots.getInstance().getRobotMenuTitle().replace("%type%", robot.getType())));
        ItemBuilder placeholder = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(C.color("&8"));
        menu.fill(0, BossRobots.getInstance().getRobotMenuSize(), placeholder);

        RobotTypes robotTypes = BossRobots.getInstance().getRobotConfig().get(robot.getType());


        BossRobots.getInstance().getMenuItemsList().forEach(menuItems -> {
            if (menuItems.getType().equals("NONE")) {
                menu.addButton(menuItems.getSlot(), new ItemButton(menuItems.getItem()) {
                    @Override
                    public void onClick(InventoryClickEvent e) {

                    }
                });
            } else if (menuItems.getType().equals("UPGRADE_FORTUNE")) {
                List<String> lore = new ArrayList<>();

                BigDecimal cost = BigDecimal.valueOf((robotTypes.getArmorStand().getCostMultiFortune() * robot.getFortuneLvl()) +
                        robotTypes.getArmorStand().getDefaultCostFortune());

                menuItems.getLore().forEach(strings -> {

                    lore.add(strings.replaceAll("%level%", robot.getFortuneLvl() + "")
                            .replaceAll("%maxlevel%", robotTypes.getArmorStand().getFortuneMaxLevel() + "")
                            .replaceAll("%cost%", BossRobots.getInstance().getNumberFormat().fixMoney(cost.toBigInteger()) + ""));
                });

                ItemBuilder item = new ItemBuilder(menuItems.getItem());
                item.setName(menuItems.getName().replaceAll("%level%", robot.getFortuneLvl() + ""));
                item.addLore(lore);

                menu.addButton(menuItems.getSlot(), new ItemButton(item) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        if(robot.getFortuneLvl() < robotTypes.getArmorStand().getFortuneMaxLevel()) {

                            BigDecimal balance = new BigDecimal(api.getTokenBalance(player));

                            if(balance.compareTo(cost) >= 1) {
                                api.removeTokens(player, cost.toBigInteger());
                                robot.upgradeFortuneLvl();
                                TL.FORTUNE_UPGRADE.send(player, new PlaceHolder("%level%", robot.getFortuneLvl()));
                                PlayerData.save(player.getUniqueId().toString());
                                openMenu(player, robot);
                            } else {
                                TL.NOT_ENOUGH_MONEY.send(player);
                            }
                        }
                    }
                });
            } else if (menuItems.getType().equals("UPGRADE_EFFICIENCY")) {
                List<String> lore = new ArrayList<>();

                BigDecimal cost = BigDecimal.valueOf((robotTypes.getArmorStand().getCostMultiEff() * robot.getEffLvl()) +
                        robotTypes.getArmorStand().getDefaultCostEff());

                menuItems.getLore().forEach(strings -> {

                    lore.add(strings.replaceAll("%level%", robot.getEffLvl() + "")
                            .replaceAll("%maxlevel%", robotTypes.getArmorStand().getEffMaxLevel() + "")
                            .replaceAll("%cost%", BossRobots.getInstance().getNumberFormat().fixMoney(cost.toBigInteger()) + ""));
                });

                ItemBuilder item = new ItemBuilder(menuItems.getItem());
                item.setName(menuItems.getName().replaceAll("%level%", robot.getEffLvl() + ""));
                item.addLore(lore);

                menu.addButton(menuItems.getSlot(), new ItemButton(item) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        if(robot.getEffLvl() < robotTypes.getArmorStand().getEffMaxLevel()) {

                            BigDecimal balance = new BigDecimal(api.getTokenBalance(player));
                            if(balance.compareTo(cost) >= 1) {
                                api.removeTokens(player,  cost.toBigInteger());
                                robot.upgradeEffLvl();
                                TL.EFFICIENCY_UPGRADE.send(player, new PlaceHolder("%level%", robot.getEffLvl()));
                                PlayerData.save(player.getUniqueId().toString());
                                openMenu(player, robot);
                            } else {
                                TL.NOT_ENOUGH_MONEY.send(player);
                            }
                        }
                    }
                });
            } else if (menuItems.getType().equals("BANK_WITHDRAW")) {

                List<String> lore = new ArrayList<>();
                menuItems.getLore().forEach(strings -> {
                    lore.add(strings.replaceAll("%amount%", BossRobots.getInstance().getNumberFormat().fixMoney(robot.getBank()) + ""));
                });

                ItemBuilder item = new ItemBuilder(menuItems.getItem());
                item.setName(menuItems.getName().replaceAll("%amount%", BossRobots.getInstance().getNumberFormat().fixMoney(robot.getBank()) + ""));
                item.addLore(lore);

                menu.addButton(menuItems.getSlot(), new ItemButton(item) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        BigInteger amt = robot.getBank();
                        TL.WITHDRAW_BANK.send(player, new PlaceHolder("%amount%", BossRobots.getInstance().getNumberFormat().fixMoney(amt)));
                        api.addRawTokens(player, amt);
                        robot.setBankAmount(BigInteger.valueOf(0));
                        openMenu(player, robot);
                        PlayerData.save(player.getUniqueId().toString());
                    }
                });
            } else if(menuItems.getType().equals("ROBOT_PICKUP")) {
                ItemBuilder item = new ItemBuilder(menuItems.getItem());
                menu.addButton(menuItems.getSlot(), new ItemButton(item) {
                    @Override
                    public void onClick(InventoryClickEvent e) {
                        Location location = BossRobots.getInstance().getLocationUtility().parseToLocation(RobotData.robotLocations.get(BossRobots.getInstance().getLocationUtility().parseToString(robot.getLocation())));

                        location.getBlock().setType(Material.AIR);
                        Location robotArmorStand = robot.getLocation();

                        robotArmorStand.getWorld().getNearbyEntities(robotArmorStand, 0.5,0.5,0.5).forEach(entity ->{
                            if(entity.getType().equals(EntityType.ARMOR_STAND)) {
                                entity.remove();
                            }
                        });

                        UUID uuid = UUID.fromString(RobotData.robotList.get((BossRobots.getInstance().getLocationUtility().parseToString(robot.getLocation()))));

                        String value = PlayerData.playerRobots.get(uuid).get(BossRobots.getInstance().getLocationUtility().parseToString(robot.getLocation())).getFortuneLvl()
                                + ":"
                                + PlayerData.playerRobots.get(uuid).get(BossRobots.getInstance().getLocationUtility().parseToString(robot.getLocation())).getEffLvl();

                        RobotData.robotList.remove(BossRobots.getInstance().getLocationUtility().parseToString(robot.getLocation()));
                        RobotData.robotLocations.remove(BossRobots.getInstance().getLocationUtility().parseToString(robot.getLocation()));
                        PlayerData.playerRobots.get(uuid).remove(BossRobots.getInstance().getLocationUtility().parseToString(robot.getLocation()));
                        player.closeInventory();
                        ItemStack robot = robotTypes.getRobot();
                        NBTItem robotNBT = new NBTItem(robot);
                        robotNBT.setString("level", value);

                        robot = robotNBT.getItem();
                        robot.setAmount(1);
                        player.getInventory().addItem(robot);
                        PlayerData.save(uuid.toString());
                    }
                });
            }
        });
        menu.open(player);
    }
}
