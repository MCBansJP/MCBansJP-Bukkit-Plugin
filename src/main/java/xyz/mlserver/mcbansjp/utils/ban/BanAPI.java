package xyz.mlserver.mcbansjp.utils.ban;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.mlserver.logapi.LogAPI;
import xyz.mlserver.mcbansjp.MCBansJP;
import xyz.mlserver.mcbansjp.utils.MojangAPI;
import xyz.mlserver.mcbansjp.utils.java.HttpAPI;
import xyz.mlserver.mcbansjp.utils.java.JsonAPI;

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

    public static boolean ban(CommandSender sender, Type type, String target, String target_uuid, BanType reason, String memo) {
        String temp_uuid;
        for (Player all : sender.getServer().getOnlinePlayers()) {
            if (target.length() <= 16) {
                if (all.getName().equalsIgnoreCase(target)) {
                    if (type == Type.GLOBAL) return BanAPI.globalBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                    else if (type == Type.LOCAL) return BanAPI.localBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                    else if (type == Type.TEMP) return BanAPI.tempBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                }
            } else {
                temp_uuid = all.getUniqueId().toString().replace("-", "");
                if (temp_uuid.equalsIgnoreCase(target_uuid.replace("-", ""))) {
                    if (type == Type.GLOBAL) return BanAPI.globalBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                    else if (type == Type.LOCAL) return BanAPI.localBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                    else if (type == Type.TEMP) return BanAPI.tempBan(sender, all.getName(), all.getUniqueId(), reason, memo);
                }
            }
        }
        if (target.length() <= 16) {
            if (type == Type.GLOBAL) return BanAPI.globalBan(sender, target, reason, memo);
            else if (type == Type.LOCAL) return BanAPI.localBan(sender, target, reason, memo);
            else if (type == Type.TEMP) return BanAPI.tempBan(sender, target, reason, memo);
        } else {
            temp_uuid = target.replace("-", "");
            if (temp_uuid.equalsIgnoreCase(target_uuid.replace("-", ""))) {
                if (type == Type.GLOBAL) return BanAPI.globalBan(sender, target, reason, memo);
                else if (type == Type.LOCAL) return BanAPI.localBan(sender, target, reason, memo);
                else if (type == Type.TEMP) return BanAPI.tempBan(sender, target, reason, memo);
            }
        }
        sender.sendMessage("§cFailed to ban " + target + ".");
        return false;
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
            json.append("{\"data\":");
            json.append("{");
            json.append("\"uuid\":\"").append(uuid.toString()).append("\",");
            json.append("\"name\":\"").append(playerName).append("\",");
            json.append("\"type\":\"").append("global").append("\",");
            json.append("\"reason_type\":\"").append(reason.getReason()).append("\",");
            json.append("\"reason\":\"").append(memo).append("\",");
            json.append("\"token\":\"").append(getApiKey()).append("\"");
            json.append("}");
            json.append("}");
            try {
                String response = HttpAPI.postResult(apiUrl + "global-ban.php", json.toString());
                LogAPI.debug(response);
                JsonNode jsonNode = JsonAPI.parseJson(response);
                if (jsonNode.get("status").asText().equalsIgnoreCase("success")) {
                    sender.sendMessage("§a" + playerName + " has been banned.");
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sender.sendMessage("§cFailed to ban " + playerName + ".");
        return false;
    }

    public static boolean localBan(CommandSender sender, String playerName, BanType reason, String memo) {
        UUID uuid = MojangAPI.getUUID(playerName);
        return localBan(sender, playerName, uuid, reason, memo);
    }

    public static boolean localBan(CommandSender sender, UUID uuid, BanType reason, String memo) {
        String playerName = MojangAPI.getName(uuid.toString());
        return localBan(sender, playerName, uuid, reason, memo);
    }

    public static boolean localBan(CommandSender sender, String playerName, UUID uuid, BanType reason, String memo) {

        return false;
    }

    public static boolean tempBan(CommandSender sender, String playerName, BanType reason, String memo) {
        UUID uuid = MojangAPI.getUUID(playerName);
        return tempBan(sender, playerName, uuid, reason, memo);
    }

    public static boolean tempBan(CommandSender sender, UUID uuid, BanType reason, String memo) {
        String playerName = MojangAPI.getName(uuid.toString());
        return tempBan(sender, playerName, uuid, reason, memo);
    }

    public static boolean tempBan(CommandSender sender, String playerName, UUID uuid, BanType reason, String memo) {

        return false;
    }

    public enum Type {
        GLOBAL,
        LOCAL,
        TEMP
    }
}
