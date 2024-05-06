package com.unclecole.bossrobots.cmd.subs;

import com.unclecole.bossrobots.BossRobots;
import com.unclecole.bossrobots.cmd.AbstractCommand;
import com.unclecole.bossrobots.utils.PlaceHolder;
import com.unclecole.bossrobots.utils.TL;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListTypeCmd extends AbstractCommand {

    public ListTypeCmd() {
        super("listtype", false);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        BossRobots.getInstance().getRobotConfig().forEach((s, robotTypes) -> {
            TL.LIST_ROBOT_TYPES.send(sender, new PlaceHolder("%type%", s));
        });
        return false;
    }

    @Override
    public String getDescription() {
        return "Default Robots Cmd";
    }

    @Override
    public String getPermission() {
        return "bossrobots.listtype";
    }
}
