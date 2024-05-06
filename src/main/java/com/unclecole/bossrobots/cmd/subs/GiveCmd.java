package com.unclecole.bossrobots.cmd.subs;

import com.unclecole.bossrobots.BossRobots;
import com.unclecole.bossrobots.cmd.AbstractCommand;
import com.unclecole.bossrobots.utils.PlaceHolder;
import com.unclecole.bossrobots.utils.TL;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCmd extends AbstractCommand {

    public GiveCmd() {
        super("give", false);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(args.length < 2) {
            TL.INVALID_COMMAND_USAGE.send(sender, new PlaceHolder("<command>", "/robots give <name> <type> <amount>"));
            return false;
        }
        int amount = 1;
        if(args[2] != null) {
            amount = Integer.parseInt(args[2]);
        }
        if(Bukkit.getPlayer(args[0]) == null) {
            TL.INVALID_ARGUMENT_PLAYER.send(sender, new PlaceHolder("<argument>", "<name>"));
            return false;
        }
        String type = args[1];
        if(BossRobots.getInstance().getRobotConfig().get(type) == null) {
            TL.INVALID_ARGUMENT_TYPE.send(sender, new PlaceHolder("<argument>", "<type>"));
            return false;
        }
        Player playerGive = Bukkit.getPlayer(args[0]);

        ItemStack itemStack = BossRobots.getInstance().getRobotConfig().get(type).getRobot();
        itemStack.setAmount(amount);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("level", "1:1");
        itemStack = nbtItem.getItem();
        playerGive.getPlayer().getInventory().addItem(itemStack);

        return false;
    }

    @Override
    public String getDescription() {
        return "Default Robots Cmd";
    }

    @Override
    public String getPermission() {
        return "bossrobots.robots";
    }
}
