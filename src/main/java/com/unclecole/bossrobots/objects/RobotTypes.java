package com.unclecole.bossrobots.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RobotTypes {

    private RobotPhysical robotPhysical;
    private RobotArmorStand robotArmorStand;

    public RobotTypes(RobotPhysical robotPhysical, RobotArmorStand robotArmorStand) {
        this.robotPhysical = robotPhysical;
        this.robotArmorStand = robotArmorStand;
    }


    public ItemStack getRobot() {
        return robotPhysical.getRobot();
    }

    public RobotArmorStand getArmorStand() {
        return robotArmorStand;
    }
}
