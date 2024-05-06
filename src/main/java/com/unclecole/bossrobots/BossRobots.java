package com.unclecole.bossrobots;

import com.unclecole.bossrobots.cmd.BaseCommand;
import com.unclecole.bossrobots.database.PlayerData;
import com.unclecole.bossrobots.database.RobotData;
import com.unclecole.bossrobots.database.serializer.Persist;
import com.unclecole.bossrobots.listeners.JoinQuitListener;
import com.unclecole.bossrobots.listeners.PlaceRobot;
import com.unclecole.bossrobots.listeners.RobotBreakListener;
import com.unclecole.bossrobots.objects.*;
import com.unclecole.bossrobots.task.MoneyTask;
import com.unclecole.bossrobots.utilities.LocationUtility;
import com.unclecole.bossrobots.utils.C;
import com.unclecole.bossrobots.utils.NumberFormat;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.itemutils.ItemBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class BossRobots extends JavaPlugin {

    @Getter public static BossRobots instance;

    @Getter private HashMap<String, String> robotList;
    @Getter private HashMap<String, RobotTypes> robotConfig;
    @Getter private ArrayList<MenuItems> menuItemsList;
    @Getter private ArrayList<String> allowedWorlds;
    @Getter private static Persist persist;
    @Getter private LocationUtility locationUtility;
    @Getter private ArrayList<UUID> activeMenuPlayers;

    @Getter private String robotMenuTitle;
    @Getter private int robotMenuSize;
    @Getter private NumberFormat numberFormat;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        persist = new Persist();
        saveDefaultConfig();
        numberFormat = new NumberFormat(this);
        locationUtility = new LocationUtility();
        robotList = new HashMap<>();
        robotConfig = new HashMap<>();
        menuItemsList = new ArrayList<>();
        allowedWorlds = new ArrayList<>();
        activeMenuPlayers = new ArrayList<>();
        File playerDataFolder = new File(this.getDataFolder(), "playerdata");
        if(!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
        loadConfig();
        Objects.requireNonNull(getCommand("robots")).setExecutor(new BaseCommand());
        this.getServer().getPluginManager().registerEvents(new PlaceRobot(), this);
        this.getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new RobotBreakListener(), this);

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new MoneyTask(), 20L, 20L * getConfig().getInt("Interval"));

        RobotData.load();
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData.load(player.getUniqueId().toString());
        });
    }

    @Override
    public void onDisable() {
        //Set a timer to save
        RobotData.save();
    }


    public void loadConfig() {
        allowedWorlds = (ArrayList<String>) getConfig().getStringList("AllowedWorlds");

        for(String key : getConfig().getConfigurationSection("Robots").getKeys(false)) {
            robotConfig.put(key, new RobotTypes(
                    new RobotPhysical(Material.getMaterial(getConfig().getString("Robots." + key + ".Physical.Material")),
                            getConfig().getString("Robots." + key + ".Physical.Name"),
                            getConfig().getStringList("Robots." + key + ".Physical.Lore"),
                            getConfig().getBoolean("Robots." + key + ".Physical.Enchanted")),
                    new RobotArmorStand(getConfig().getBoolean("Robots." + key + ".ArmorStand.Animation"),
                            Material.getMaterial(getConfig().getString("Robots." + key + ".ArmorStand.Material")),
                            Material.getMaterial(getConfig().getString("Robots." + key + ".ArmorStand.IN_HAND")),
                            Material.getMaterial(getConfig().getString("Robots." + key + ".ArmorStand.Helmet")),
                            Material.getMaterial(getConfig().getString("Robots." + key + ".ArmorStand.Chestplate")),
                            Material.getMaterial(getConfig().getString("Robots." + key + ".ArmorStand.Leggings")),
                            Material.getMaterial(getConfig().getString("Robots." + key + ".ArmorStand.Boots")),
                            getConfig().getString("Robots." + key + ".ArmorStand.Name"),
                            getConfig().getDouble("Robots." + key + ".ArmorStand.MoneyPerMinute"),
                            getConfig().getInt("Robots." + key + ".ArmorStand.EffMaxLevel"),
                            getConfig().getInt("Robots." + key + ".ArmorStand.FortuneMaxLevel"),
                            getConfig().getInt("Robots." + key + ".ArmorStand.DefaultCostEff"),
                            getConfig().getInt("Robots." + key + ".ArmorStand.DefaultCostFortune"),
                            getConfig().getDouble("Robots." + key + ".ArmorStand.CostMultiEff"),
                            getConfig().getInt("Robots." + key + ".ArmorStand.CostMultiFortune"))));
        }

        for(String key : getConfig().getConfigurationSection("RobotMenu.Items").getKeys(false)) {
            ItemBuilder item = new ItemBuilder(Material.getMaterial(getConfig().getString("RobotMenu.Items." + key + ".Material")))
                    .setName(C.color(getConfig().getString("RobotMenu.Items." + key + ".Title")))
                    .addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_DESTROYS,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_DYE);
            if(getConfig().getInt("RobotMenu.Items." + key + ".Durability") >= 0) {
                item.setDurability(getConfig().getInt("RobotMenu.Items." + key + ".Durability"));
            }
            if(getConfig().getInt("RobotMenu.Items." + key + ".CustomModelData") >= 0) {
                item.setCustomModelData(getConfig().getInt("RobotMenu.Items." + key + ".CustomModelData"));
            }
            if(getConfig().getBoolean("RobotMenu.Items." + key + ".Enchanted") == true) {
                item.addEnchant(Enchantment.DIG_SPEED,1);
            }

            menuItemsList.add(new MenuItems(getConfig().getString("RobotMenu.Items." + key + ".Type"),
                    getConfig().getInt("RobotMenu.Items." + key + ".Slot"), C.color(getConfig().getString("RobotMenu.Items." + key + ".Title")),
                    C.color(getConfig().getStringList("RobotMenu.Items." + key + ".Lore")), item));
        }

        robotMenuTitle = getConfig().getString("RobotMenu.Title");
        robotMenuSize = getConfig().getInt("RobotMenu.Size");

    }

    public Robot getRobotFromLocation(Location location) {
        String loc = this.getLocationUtility().parseToString(location);

        UUID uuid = UUID.fromString(RobotData.robotList.get(loc));
        if(!PlayerData.playerRobots.containsKey(uuid)) {
            PlayerData.load(uuid.toString());
        }

        return PlayerData.playerRobots.get(uuid).get(loc);
    }
}