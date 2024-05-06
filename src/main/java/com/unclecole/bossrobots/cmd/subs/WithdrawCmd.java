package com.unclecole.bossrobots.cmd.subs;

import com.unclecole.bossrobots.BossRobots;
import com.unclecole.bossrobots.cmd.AbstractCommand;
import com.unclecole.bossrobots.database.PlayerData;
import com.unclecole.bossrobots.utils.PlaceHolder;
import com.unclecole.bossrobots.utils.TL;
import me.taleeko.bossenchants.BossEnchants;
import me.taleeko.bossenchants.api.TokenAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class WithdrawCmd extends AbstractCommand {
    BossEnchants bossEnchants;
    TokenAPI api;

    public WithdrawCmd() {
        super("withdraw", true);
        bossEnchants = BossEnchants.getInstance();
        api = new TokenAPI(bossEnchants);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(PlayerData.playerRobots.get(player.getUniqueId()).isEmpty()) {
            //Send No Robots Message...
        }

        AtomicReference<BigInteger> total = new AtomicReference<>(BigInteger.valueOf(0));
        PlayerData.playerRobots.get(player.getUniqueId()).forEach((s, robot) -> {
            BigInteger amt = total.get();
            amt = amt.add(robot.getBank());
            total.getAndSet(amt);
            robot.setBankAmount(BigInteger.valueOf(0L));
        });
        TL.WITHDRAW_MONEY_FROM_ROBOTS.send(player, new PlaceHolder("%amount%", BossRobots.getInstance().getNumberFormat().fixMoney(total.get())), new PlaceHolder("%robots%", PlayerData.playerRobots.get(player.getUniqueId()).size()));
        api.addTokens(player, total.get());
        return false;
    }

    @Override
    public String getDescription() {
        return "Default Robots Cmd";
    }

    @Override
    public String getPermission() {
        return "bossrobots.withdrawall";
    }
}
