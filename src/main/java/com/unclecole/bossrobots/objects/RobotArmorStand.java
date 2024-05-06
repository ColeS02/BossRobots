package com.unclecole.bossrobots.objects;

import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

public class RobotArmorStand {

    private boolean animation;
    @Getter private Material mineBlock, helmet, chestplate, leggings, boots;
    @Getter private Material inHand;
    @Getter private String name;
    @Getter private double moneyPerMinute;
    @Getter private int effMaxLevel;
    @Getter private int fortuneMaxLevel;
    @Getter private int defaultCostEff;
    @Getter private int defaultCostFortune;
    @Getter private double costMultiEff;
    @Getter private double costMultiFortune;


    public RobotArmorStand(boolean animation, Material mineBlock, Material inHand, Material helmet, Material chestplate, Material leggings, Material boots, String name,
                           double moneyPerMinute, int effMaxLevel, int fortuneMaxLevel, int defaultCostEff, int defaultCostFortune, double costMultiEff, double costMultiFortune) {
        this.animation = animation;
        this.mineBlock = mineBlock;
        this.inHand = inHand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.name = name;
        this.moneyPerMinute = moneyPerMinute;
        this.effMaxLevel = effMaxLevel;
        this.fortuneMaxLevel = fortuneMaxLevel;
        this.defaultCostEff = defaultCostEff;
        this.defaultCostFortune = defaultCostFortune;
        this.costMultiEff = costMultiEff;
        this.costMultiFortune = costMultiFortune;
    }

    public Material getMineBlock() {
        return mineBlock;
    }

}
