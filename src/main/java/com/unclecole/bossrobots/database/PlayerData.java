package com.unclecole.bossrobots.database;

import com.unclecole.bossrobots.BossRobots;
import com.unclecole.bossrobots.database.serializer.BukkitSerialization;
import com.unclecole.bossrobots.database.serializer.Persist;
import com.unclecole.bossrobots.database.serializer.Serializer;
import com.unclecole.bossrobots.objects.Robot;
import org.bukkit.Location;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    public static transient PlayerData instance = new PlayerData();

    public static transient HashMap<UUID, HashMap<String, Robot>> playerRobots = new HashMap<>();

    public static HashMap<String, Robot> robots;

    public static void save(String uuid) {
        robots = playerRobots.get(UUID.fromString(uuid));
        new Persist().save(instance, "/playerdata/" + uuid);
    }

    public static void load(String uuid) {
        robots = new HashMap<>();
        new Serializer().load(instance, PlayerData.class, "/playerdata/" + uuid);
        robots.forEach((loc, robot) -> {
            if(!RobotData.robotLocations.containsKey(loc) || !RobotData.robotList.containsKey(loc)) {
                BossRobots.getInstance().getLogger().warning("Broken Robot (FIXING): " + loc);
                RobotData.robotLocations.put(loc, robot.getRawLocation());
                RobotData.robotList.put(loc, uuid);
            }
        });
        playerRobots.put(UUID.fromString(uuid), robots);
    }

}