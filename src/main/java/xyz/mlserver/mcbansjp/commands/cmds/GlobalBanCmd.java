package xyz.mlserver.mcbansjp.commands.cmds;

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
            if (BanType.Local_Rule.getCommandType().equalsIgnoreCase(type) || BanType.Local_Rule.getShortcutCode().equalsIgnoreCase(type)) {
                sender.sendMessage("§cLocal Rule ban is not supported. Plese use /localban command.");
                return true;
            } else if (type.equalsIgnoreCase("h") || type.equalsIgnoreCase("hacking")) {
                return BanAPI.ban(sender, BanAPI.Type.GLOBAL, target, target_uuid, BanType.HACKING, reason);
            } else if (type.equalsIgnoreCase("o") || type.equalsIgnoreCase("other")) {
                return BanAPI.ban(sender, BanAPI.Type.GLOBAL, target, target_uuid, BanType.OTHER, reason);
            } else {
                for (BanType banType : BanType.values()) {
                    if (banType.getCommandType().equalsIgnoreCase(type) || banType.getShortcutCode().equalsIgnoreCase(type)) {
                        return BanAPI.ban(sender, BanAPI.Type.GLOBAL, target, target_uuid, banType, reason);
                    }
                }
                sender.sendMessage("§cUsage: /gban <player|uuid> <type> [reason]");
                return true;
            }
        }
    }
}
