package com.unclecole.bossrobots.database;

import com.google.common.collect.Maps;
import com.unclecole.bossrobots.database.serializer.Serializer;
import org.bukkit.Location;

import java.util.*;

public class RobotData {

    public static transient RobotData instance = new RobotData();

    //Location - UUID
    public static HashMap<String, String> robotList = Maps.newHashMap();
    public static HashMap<String, String> robotLocations = new HashMap<>();

    public static void save() {
        new Serializer().save(instance);
    }

    public static void load() {
        new Serializer().load(instance, RobotData.class, "robotdata");
    }

}