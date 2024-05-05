package xyz.mlserver.mcbansjp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mlserver.mcbansjp.utils.ban.BanAPI;
import xyz.mlserver.mcbansjp.utils.ban.BanType;

public class GlobalBanCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /gban <player|uuid> <type> <reason>");
            return true;
        } else {
            String target_uuid = args[0].replace("-", "");
            String target = args[0];
            String type = args[1];
            String reason = args[2];
            boolean success = false;
            for (int i = 3; i < args.length; i++) reason += " " + args[i];
            if (type.equalsIgnoreCase("g") || type.equalsIgnoreCase("griefing")) {
                return ban(sender, target, target_uuid, BanType.GRIEFING, reason);
            } else if (type.equalsIgnoreCase("h") || type.equalsIgnoreCase("hacking")) {
                return ban(sender, target, target_uuid, BanType.HACKING, reason);
            } else if (type.equalsIgnoreCase("o") || type.equalsIgnoreCase("other")) {
                return ban(sender, target, target_uuid, BanType.OTHER, reason);
            } else {
                sender.sendMessage("§cUsage: /gban <player|uuid> <type> [reason]");
                return true;
            }
        }
    }

    private static boolean ban(CommandSender sender, String target, String target_uuid, BanType reason, String memo) {
        String temp_uuid;
        for (Player all : sender.getServer().getOnlinePlayers()) {
            if (target.length() <= 16) {
                if (all.getName().equalsIgnoreCase(target)) {
                    return BanAPI.globalBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                }
            } else {
                temp_uuid = all.getUniqueId().toString().replace("-", "");
                if (temp_uuid.equalsIgnoreCase(target_uuid)) {
                    return BanAPI.globalBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                }
            }
        }
        return BanAPI.globalBan(sender, target, reason, memo);
    }
}
