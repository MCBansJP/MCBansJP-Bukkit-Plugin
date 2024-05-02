package xyz.mlserver.mcbansjp.utils.ban;

import xyz.mlserver.mcbansjp.listeners.BukkitPlayerJoinListener;
import xyz.mlserver.mcbansjp.utils.MojangAPI;

import java.net.URL;
import java.util.UUID;

public class BanAPI {

    private static final String apiUrl = "https://api-mcbansjp.mlserver.xyz/";


    public static boolean globalBan(String playerName, BanReason reason) {
        UUID uuid = MojangAPI.getUUID(playerName);
        return globalBan(playerName, uuid, reason);
    }

    public static boolean globalBan(UUID uuid, BanReason reason) {
        String playerName = MojangAPI.getName(uuid.toString());
        return globalBan(playerName, uuid, reason);
    }

    public static boolean globalBan(String playerName, UUID uuid, BanReason reason) {
        try {
            URL url = new URL(apiUrl + "global-ban.php?uuid=" + uuid.toString() + "&player=" + playerName + "&reason=" + reason.getReason());
            url.openConnection().connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean localBan(String playerName, BanReason reason) {
        UUID uuid = MojangAPI.getUUID(playerName);
        return localBan(playerName, uuid, reason);
    }

    public static boolean localBan(UUID uuid, BanReason reason) {
        String playerName = MojangAPI.getName(uuid.toString());
        return localBan(playerName, uuid, reason);
    }

    public static boolean localBan(String playerName, UUID uuid, BanReason reason) {


        return false;
    }
}
