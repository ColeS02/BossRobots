package com.unclecole.bossrobots.objects;

import com.unclecole.bossrobots.BossRobots;
import org.bukkit.Location;

import java.math.BigInteger;

public class Robot {

    private final String type;
    private String uuid;
    private final String location;
    private final String blockLocation;
    private int effLvl;
    private int fortuneLvl;
    private BigInteger bank;

    public Robot(String type, String uuid, String location, String blockLocation) {
        this.type = type;
        this.uuid = uuid;
        this.location = location;
        this.blockLocation = blockLocation;
        this.effLvl = 1;
        this.fortuneLvl = 1;
        this.bank = BigInteger.valueOf(0);
    }

    public String getType() {
        return type;
    }

    public int getEffLvl() {
        return effLvl;
    }

    public void upgradeEffLvl() { effLvl++; }

    public void upgradeFortuneLvl() {fortuneLvl++; }

    public int getFortuneLvl() {
        return fortuneLvl;
    }

    public String getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return BossRobots.getInstance().getLocationUtility().parseToLocation(location);
    }

    public String getRawLocation() {
        return location;
    }

    public BigInteger getBank() {
        return bank;
    }

    public void setEffLvl(int effLvl) {
        this.effLvl = effLvl;
    }

    public void setFortuneLvl(int fortuneLvl) {
        this.fortuneLvl = fortuneLvl;
    }

    public void setOwner(String uuid) {
        this.uuid = uuid;
    }

    public void addBankAmount(BigInteger amount) {
        this.bank = bank.add(amount);
    }

    public void setBankAmount(BigInteger amount) {
        this.bank = amount;
    }

    public void takeBankAmount(BigInteger amount) {
        this.bank = bank.subtract(amount);
    }

    public Location getBlockLocation() {
        return BossRobots.getInstance().getLocationUtility().parseToLocation(blockLocation);
    }


}
