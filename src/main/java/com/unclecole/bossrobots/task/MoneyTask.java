package com.unclecole.bossrobots.task;

import com.unclecole.bossrobots.BossRobots;
import com.unclecole.bossrobots.database.PlayerData;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MoneyTask implements Runnable{

    @Override
    public void run() {
        if(PlayerData.playerRobots.isEmpty()) return;

        PlayerData.playerRobots.forEach((uuid, robots) -> {
            if(Bukkit.getPlayer(uuid) == null) return;
            robots.forEach((location, robot) -> {

                BigDecimal moneyPerMinute = BigDecimal.valueOf(BossRobots.getInstance().getRobotConfig().get(robot.getType()).getArmorStand().getMoneyPerMinute());

                BigInteger money = (moneyPerMinute.multiply(BigDecimal.valueOf(robot.getEffLvl())).multiply(BigDecimal.valueOf(robot.getFortuneLvl()))).toBigInteger();
                robot.addBankAmount(money);
            });
        });
    }
}
