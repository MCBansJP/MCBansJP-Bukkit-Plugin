package xyz.mlserver.mcbansjp.utils;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.UUID;

public class MojangAPI {

    /**
     * Player UUID from Player Name
     * @param playerName String
     * @return UUID
     */
    public static UUID getUUID(String playerName) {
        // URLを叩くとラグが発生するため
        for (Player p : Bukkit.getOnlinePlayers()) if (p.getName().equalsIgnoreCase(playerName)) return p.getUniqueId();
        String output = callURL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
        StringBuilder result = new StringBuilder();
        readData(output, result);
        String u = result.toString();
        System.out.println(u);
        StringBuilder uuid = new StringBuilder();
        for(int i = 0; i <= 31; i++) {
            uuid.append(u.charAt(i));
            if(i == 7 || i == 11 || i == 15 || i == 19) uuid.append("-");
        }
        return UUID.fromString(uuid.toString());
    }

    /**
     * Player Name from Player UUID
     * @param uuid UUID
     * @return String
     */
    public static String getName(String uuid) {
        String url = "https://api.mojang.com/user/profiles/"+uuid.replace("-", "")+"/names";
        try {
            @SuppressWarnings("deprecation")
            String nameJson = IOUtils.toString(new URL(url));
            JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
            String playerSlot = nameValue.get(nameValue.size()-1).toString();
            JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
            return nameObject.get("name").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private static void readData(String toRead, StringBuilder result) {
        int i = 7;
        while(i < 200) {
            if(!String.valueOf(toRead.charAt(i)).equalsIgnoreCase("\"")) result.append(toRead.charAt(i));
            else break;
            i++;
        }
    }

    private static String callURL(String URL) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn;
        InputStreamReader in = null;
        try {
            java.net.URL url = new URL(URL);
            urlConn = url.openConnection();
            if (urlConn != null) urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                int cp;
                while ((cp = bufferedReader.read()) != -1) sb.append((char) cp);
                bufferedReader.close();
            }
            Objects.requireNonNull(in).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
