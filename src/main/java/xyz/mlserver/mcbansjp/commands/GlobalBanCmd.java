package xyz.mlserver.mcbansjp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlobalBanCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /gban <player|uuid> <type> <reason>");
            return true;
        } else {
            String target = args[0];
            String type = args[1];
            String reason = args[2];
            for (int i = 3; i < args.length; i++) reason += " " + args[i];
            if (target.length() <= 16) {

            }
        }
        return false;
    }
}
