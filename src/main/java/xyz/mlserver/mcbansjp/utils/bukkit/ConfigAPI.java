package xyz.mlserver.mcbansjp.utils.bukkit;

import xyz.mlserver.mcbansjp.MCBansJP;
import xyz.mlserver.mcbansjp.utils.CustomConfiguration;
import xyz.mlserver.mcbansjp.utils.ban.BanAPI;

public class ConfigAPI {

    public static void load(CustomConfiguration config) {
        MCBansJP.updateCheck = config.getConfig().getBoolean("update-checker", true);

        BanAPI.setApiKey(config.getConfig().getString("api-key"));
    }


}
