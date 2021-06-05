package net.skulluhc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("invaded.admin")) return true;

        LavaTickModifier.INSTANCE.setDebug(Boolean.parseBoolean(args[0]));
        sender.sendMessage("now " + LavaTickModifier.INSTANCE.isDebug());
        return true;
    }
}
