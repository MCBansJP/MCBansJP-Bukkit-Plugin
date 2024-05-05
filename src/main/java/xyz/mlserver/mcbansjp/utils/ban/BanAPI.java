package xyz.mlserver.mcbansjp.utils.ban;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mlserver.mcbansjp.MCBansJP;
import xyz.mlserver.mcbansjp.utils.MojangAPI;
import xyz.mlserver.mcbansjp.utils.java.HttpAPI;

import java.io.IOException;
import java.util.UUID;

public class BanAPI {

    private static final String apiUrl = "https://api-mcbansjp.mlserver.xyz/";

    private static String apiKey = null;

    public static void setApiKey(String apiKey) {
        BanAPI.apiKey = apiKey;
    }

    public static String getApiKey() {
        return apiKey;
    }


    public static boolean globalBan(CommandSender sender, String playerName, BanType reason, String memo) {
        UUID uuid = MojangAPI.getUUID(playerName);
        return globalBan(sender, playerName, uuid, reason, memo);
    }

    public static boolean globalBan(CommandSender sender, UUID uuid, BanType reason, String memo) {
        String playerName = MojangAPI.getName(uuid.toString());
        return globalBan(sender, playerName, uuid, reason, memo);
    }

    public static boolean globalBan(CommandSender sender, String playerName, UUID uuid, BanType reason, String memo) {
        OfflinePlayer player = MCBansJP.getPlugin().getServer().getOfflinePlayer(uuid);
        if (player.isBanned()) {
            sender.sendMessage("§c" + playerName + " is already banned.");
            return false;
        }
        Bukkit.getBanList(BanList.Type.NAME).addBan(playerName, reason.getReason() + " | " + memo, null, sender.getName());
        for (Player all : sender.getServer().getOnlinePlayers()) {
            if (all.getName().equalsIgnoreCase(playerName)) {
                all.kickPlayer("§cYou have been banned from this server.\n§cReason: " + reason.getReason() + "\n§cMemo: " + memo);
                break;
            }
        }

        if (getApiKey() != null) {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"uuid\":\"").append(uuid.toString()).append("\",");
            json.append("\"name\":\"").append(playerName).append("\",");
            json.append("\"reason\":\"").append(reason.getReason()).append("\",");
            json.append("\"memo\":\"").append(memo).append("\",");
            json.append("\"api-key\":\"").append(getApiKey()).append("\"");
            json.append("}");
            try {
                String response = HttpAPI.postResult(apiUrl, json.toString());
                if (response.contains("\"status\": \"OK\"")) {
                    sender.sendMessage("§a" + playerName + " has been banned.");
                    return true;
                } else {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sender.sendMessage("§cFailed to ban " + playerName + ".");
        return false;
    }

    public static boolean localBan(String playerName, BanType reason) {
        UUID uuid = MojangAPI.getUUID(playerName);
        return localBan(playerName, uuid, reason);
    }

    public static boolean localBan(UUID uuid, BanType reason) {
        String playerName = MojangAPI.getName(uuid.toString());
        return localBan(playerName, uuid, reason);
    }

    public static boolean localBan(String playerName, UUID uuid, BanType reason) {


        return false;
    }
}
