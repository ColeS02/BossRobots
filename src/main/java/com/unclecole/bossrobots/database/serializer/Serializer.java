package com.unclecole.bossrobots.database.serializer;

import com.unclecole.bossrobots.BossRobots;

public class Serializer {


    /**
     * Saves your class to a .json file.
     */
    public void save(Object instance) {
        BossRobots.getPersist().save(instance);
    }

    /**
     * Loads your class from a json file
     *
   */
    public <T> T load(T def, Class<T> clazz, String name) {
        return BossRobots.getPersist().loadOrSaveDefault(def, clazz, name);
    }



}
